package issue.compose

data class User(
    override val id: String,
    val name: String,
    val age: Int
) : UniqueItem
