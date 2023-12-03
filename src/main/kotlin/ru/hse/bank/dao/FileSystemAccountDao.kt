@file:Suppress("DEPRECATION")

package ru.hse.bank.dao


import org.apache.commons.io.input.ReversedLinesFileReader
import ru.hse.bank.entity.AccountEntity
import java.nio.charset.Charset.defaultCharset
import kotlin.io.path.Path
import kotlin.io.path.exists

class FileSystemAccountDao : AccountDao {
    private var runtimeAccountDao = RuntimeAccountDao()

    companion object {
        private val DIR_WITH_ACCOUNTS = "accounts"
    }

    override fun findAccountByName(name: String): AccountEntity {
        val file = Path(DIR_WITH_ACCOUNTS, name)
        if (!file.exists()) {
            throw Exception("There is no such user: $name")
        }
        var account: AccountEntity
        try { // In case this account already was in our runtime
            account = runtimeAccountDao.findAccountByName(name)
        } catch (ex: Exception) { // if we see this account for the first time during the runtime
            account = AccountEntity(
                name, ReversedLinesFileReader(file.toFile(), defaultCharset()).readLine().toLong()
            )
            runtimeAccountDao.saveAccount(account)
        }
        return account
    }

    override fun increaseAccountSumByName(name: String, sum: Long): AccountEntity {
        try {
            return findAccountByName(name).also { it.sum += sum }
        } catch (ex: Exception) {
            throw ex
        }
    }

    override fun saveAccount(accountEntity: AccountEntity): Boolean {
        val file = Path(DIR_WITH_ACCOUNTS, accountEntity.name).toFile().also {
            it.parentFile.mkdir()
            it.appendText("${accountEntity.sum}\n", defaultCharset())
        }
        return file.exists()
    }
}
