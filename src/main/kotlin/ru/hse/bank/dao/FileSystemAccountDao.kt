package ru.hse.bank.dao

import org.apache.commons.io.FileUtils
import ru.hse.bank.entity.AccountEntity
import ru.hse.bank.dao.exception.AccountNotFoundException
import java.nio.charset.Charset
import kotlin.io.path.Path

class FileSystemAccountDao : AccountDao {

    companion object {
        private const val DIR_WITH_ACCOUNTS = "accounts"
    }

    override fun findAccountByName(name: String): AccountEntity {
        val accountFile = Path(DIR_WITH_ACCOUNTS, name).toFile()

        if (!accountFile.exists()){
            throw AccountNotFoundException("Account with name $name not found")
        }

        val value = FileUtils.readFileToString(accountFile, Charset.defaultCharset())

        return AccountEntity(
            name = name,
            sum = value.toLong()
        )
    }

    override fun increaseAccountSumByName(name: String, value: Long): AccountEntity {
        val accountEntity = findAccountByName(name)

        accountEntity.sum+=value

        saveAccount(accountEntity)

        return findAccountByName(name)
    }

    override fun saveAccount(accountEntity: AccountEntity): Boolean {
        val file = Path(DIR_WITH_ACCOUNTS, accountEntity.name).toFile()

        if (file.exists()) {
            file.delete()
        }

        file.writeText("${accountEntity.sum}", Charset.defaultCharset())

        return file.exists()
    }
}
