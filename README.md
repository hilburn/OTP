OTP
=========

`/otp` follows standard `/minecraft:tp` syntax with some of Mystcraft's `/tpx` and CoFH Core's `/cofh tpx` syntactic sugar built in.  "Player's location" refers to the current location of the player, either where they are right now or the last location they were when they logged out. If a player is moved when they are logged out they spawn in the location that you moved them to when they log in.

Command                           | Function
----------------------------------|---------------------------------------------------------------------------------------
`/otp [help]`                     | display help for `/otp`
`/otp help [command]`             | display syntax and info for selected command (use `otp` for teleport methods
`/otp <player>`                   | teleports you to the current location of `player`
`/otp <player1> <player2>`        | moves `player1` to `player2`'s location
`/otp <player> <x> <y> <z> [dim]` | moves `player` to `x, y, z` in either the dimension `dim` or the dimension the player running the command is in.
`/otp <player> <dim>`             | moves `player` to the spawn point of `dim`
`/otp list`                       | list all pending offline teleports
`/otp cancel <player>`            | cancel a pending offline teleport for `player`
`/otp clear names`                | clear list of player logout coordinates
`/otp clear tp`                   | clear all pending offline teleports

## Contributors
hilburn

## License
Licensed under the [DBaJ (Don't Be a Jerk) non-commercial care-free license](https://github.com/hilburn/CallowCraft/blob/master/LICENSE.md).
