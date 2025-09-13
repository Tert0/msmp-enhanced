# MSMP Enhanced
MSMP Enhanced is a server-side Fabric Mod that extends the [Minecraft Server Management Protocol](https://minecraft.wiki/w/Minecraft_Server_Management_Protocol) introduced in Minecraft 1.21.9 (25w35a).

This mod provides optional Unix socket support and adds additional RPC methods.

## Features
### Methods
#### Command Execution
- **Method:** `msmpenhanced:command/run`
- **Description:** Runs console commands and returns the command output as structured Text Components
- **Parameters:** `command` (String)
- **Response:** `messages` (Array of [Text Components](https://minecraft.wiki/w/Text_component_format))

#### Command Suggestion (Tab Completion)
- **Method:** `msmpenhanced:command/suggest`
- **Description:** Allows external tools (e.g. web panel or CLI tools) to provide command tab completion
- **Parameters:** `partialCommand` (String)
- **Response:** `suggestions` ([Command Suggestions](#command-suggestions))

### Unix Socket Support (Optional)
Instead of binding the Minecraft Management Server to a TCP port, MSMP Enhanced can listen on a Unix Domain Socket.
See [Config](#config) for the configuration options.


## Schemas
### Command Suggestions
- `start` (Integer): start position of the suggestions
- `end` (Integer): end position of the suggestions
- `suggestions` (Array of [Command Suggestion](#command-suggestion))

### Command Suggestion
- `text` (String): command suggestion
- `tooltip` (Optional [Text Component](https://minecraft.wiki/w/Text_component_format)): additional tooltip

## Config
Config is stored at `config/msmpenhanced.json`
```json
{
  "unixSocketEnabled": false,
  "unixSocketPath": "msmp.sock"
}
```
MSMP Enhanced will default to TCP (vanilla behavior).

## License
This project is licensed under the GNU Lesser General Public License version 3 only.