package nl.vanparerensoftwaredevelopment.saltedpassmanager.storage

/**
 * Thrown by [StoredAccountsService.remove] if at least one account
 * was not found and its `ignoreMissing` is set to `false`.
 */
class AccountNotFoundException(msg: String): RuntimeException(msg)