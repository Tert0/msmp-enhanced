package de.tert0.msmpenhanced.mixin;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import de.tert0.msmpenhanced.Config;
import de.tert0.msmpenhanced.MsmpEnhancedMod;
import io.netty.bootstrap.AbstractBootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.ServerChannel;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerDomainSocketChannel;
import io.netty.channel.unix.DomainSocketAddress;
import net.minecraft.server.dedicated.management.ManagementServer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ManagementServer.class)
public abstract class ManagementServerMixin {
    private @Unique Config config;
    private @Unique @Nullable EpollEventLoopGroup epollEventLoopGroup;

    @Inject(method = "<init>*", at = @At("RETURN"))
    void constructor(CallbackInfo ci) {
        this.config = MsmpEnhancedMod.getConfig();
        if(this.config.unixSocketEnabled()) {
            this.epollEventLoopGroup = new EpollEventLoopGroup(0, new ThreadFactoryBuilder().setNameFormat("Management server IO #%d").setDaemon(true).build());
        }
    }

    @WrapOperation(method = "listen", at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/ServerBootstrap;channel(Ljava/lang/Class;)Lio/netty/bootstrap/AbstractBootstrap;"), remap = false)
    AbstractBootstrap<ServerBootstrap, ServerChannel> channel(ServerBootstrap instance, Class<?> channelClass, Operation<AbstractBootstrap<ServerBootstrap, ServerChannel>> original) {
        if(this.config.unixSocketEnabled()) {
            return instance.channel(EpollServerDomainSocketChannel.class);
        }
        return original.call(instance, channelClass);
    }

    @WrapOperation(method = "listen", at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/ServerBootstrap;group(Lio/netty/channel/EventLoopGroup;)Lio/netty/bootstrap/ServerBootstrap;"), remap = false)
    ServerBootstrap group(ServerBootstrap instance, EventLoopGroup group, Operation<ServerBootstrap> original) {
        if(this.config.unixSocketEnabled()) {
            return instance.group(this.epollEventLoopGroup);
        }
        return original.call(instance, group);
    }

    @WrapOperation(method = "listen", at = @At(value = "INVOKE", target = "Lio/netty/bootstrap/ServerBootstrap;localAddress(Ljava/lang/String;I)Lio/netty/bootstrap/AbstractBootstrap;"), remap = false)
    AbstractBootstrap<ServerBootstrap, ServerChannel> localAddress(ServerBootstrap instance, String host, int port, Operation<AbstractBootstrap<ServerBootstrap, ServerChannel>> original) {
        if(this.config.unixSocketEnabled()) {
            return instance.localAddress(new DomainSocketAddress(this.config.unixSocketPath()));
        }
        return original.call(instance, host, port);
    }

    @ModifyArg(method = "listen", at = @At(value = "INVOKE", target = "Lorg/slf4j/Logger;info(Ljava/lang/String;Ljava/lang/Object;Ljava/lang/Object;)V"), remap = false)
    String modifyLogListenMessage(String msg) {
        if(this.config.unixSocketEnabled()) {
            return "Json-RPC Management connection listening on unix socket at " + this.config.unixSocketPath();
        }
        return msg;
    }


    @Inject(method = "stop", at = @At("TAIL"))
    void stop(boolean shutdownEventLoop, CallbackInfo ci) throws InterruptedException {
        if(this.epollEventLoopGroup != null && shutdownEventLoop) {
            this.epollEventLoopGroup.shutdownGracefully().sync();
        }
    }

    @Inject(method = "getPort", at = @At("HEAD"), cancellable = true)
    void getPort(CallbackInfoReturnable<Integer> cir) {
        if(this.config.unixSocketEnabled()) {
            cir.setReturnValue(-1);
        }
    }
}
