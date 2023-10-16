package nl.vanparerensoftwaredevelopment.saltedpassmanager.storage

/**
 * Thrown by [StoredAccountsService.add] if an account already exists
 * and its `onSameKey` is set to [StoredAccountsService.AddOnSameKey.ERROR].
 */
class AccountAlreadyExistsException(msg: String): RuntimeException(msg)