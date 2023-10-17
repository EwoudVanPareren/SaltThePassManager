# SaltThePass Manager

SaltThePass Manager is an app for Android and Desktop (Windows/Linux/Mac) that
provides a way to use the [SaltThePass](https://saltthepass.com) algorithm with
a few extra features, most notably a way to save account data (useful for
keeping track of and autofilling account-specific parameters).

Copyright 2023 Ewoud van Pareren.

### Status

Currently, the project is in a usable state. It has been tested only on
Android (phone and tablet) and Linux (Kubuntu).

Some quality-of-life features are yet to be added (see [To-Do](#to-do)),
and some more experimental libraries (and library features) are used that
will need to be updated in the future.

## Features

- Generate [SaltThePass](https://saltthepass.com) passwords
  (excluding domain name rules and RIPEMD-160 hashing algorithm)
- Some extra fields for adjusting the generated password
- Keep track of accounts (combinations of domain name, phrase and other fields)
  by saving them
- (Desktop) Keep the application open in the background (in the system tray)

## To-Do

- Test on Windows & Mac
- Add support for RIPEMD-160 hashing algorithm
- Add more tests
  - Unit tests for the `ScreenModel`s
  - UI tests
- Configure Proguard on Desktop
- Add a way to move the stored-accounts file to a user-defined location
  This would allow users to synchronize stored accounts between devices
  - One possible extension to this would be to encrypt the file with a password as well
- Add a more convenient way to view and manage saved data
- Add an app-locking feature with PIN/pattern (primarily for Android)
- On Android, add a way to persist the Master Password when the app is closed in the background
  (only allow with app-locking enabled)
- Try to make the Desktop app more lightweight (reduce memory use, minify)

## Credits

- The original [SaltThePass](https://saltthepass.com) by [Nic Jansma](https://nicj.net/) ([GitHub](https://github.com/nicjansma)).
  This project uses the same algorithm and a similarly designed UI.
  Additionally, some text from the original SaltThePass was adapted into this
  project's help texts.
- GitHub Material Icon from [Pictogrammers](https://pictogrammers.com/library/mdi/)
- Logic for opening the default browser on Desktop is based on https://stackoverflow.com/a/68426773

## Version History

- **v1.0.0** - *2023-10-17* - Initial Release
