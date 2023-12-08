package ru.hse.bank.dao

import ru.hse.bank.entity.AccountEntity

class RuntimeAccountDao : AccountDao {
    private val accountEntities = mutableListOf<AccountEntity>()

    override fun findAccountByName(name: String): AccountEntity =
        accountEntities.find { it.name == name } ?: throw RuntimeException("Account not found")

    override fun increaseAccountSumByName(name: String, sum: Long): AccountEntity =
        findAccountByName(name).also { it.sum += sum }

    override fun saveAccount(accountEntity: AccountEntity): Boolean = accountEntities.add(accountEntity)

    override fun transfer(nameFrom: String, nameTo: String, sum: Long)
            : Pair<AccountEntity, AccountEntity> {
        return transfer(findAccountByName(nameFrom), findAccountByName(nameTo), sum)
    }

    override fun transfer(
        accountEntityFrom: AccountEntity,
        accountEntityTo: AccountEntity,
        sum: Long
    ): Pair<AccountEntity, AccountEntity> {
        var newAccountEntityFrom : AccountEntity
        var newAccountEntityTo : AccountEntity

        try {
            newAccountEntityFrom = increaseAccountSumByName(accountEntityFrom.name, -sum)
        } catch (e : Exception) {
            throw Exception("not enough balance")
        }

        try {
            newAccountEntityTo = increaseAccountSumByName(accountEntityTo.name, sum)
        } catch (e:Exception) {
            newAccountEntityFrom = increaseAccountSumByName(accountEntityFrom.name, sum)
            throw Exception("something goes wrong")
        }

        println("Transaction from ${newAccountEntityFrom.name} -> ${newAccountEntityTo.name} amount ${sum}")

        return Pair(newAccountEntityFrom, newAccountEntityTo)
    }
}
