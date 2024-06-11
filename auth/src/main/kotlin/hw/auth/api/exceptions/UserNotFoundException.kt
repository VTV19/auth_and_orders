package hw.auth.api.exceptions

 class UserNotFoundException(nickname: String): Exception("Пользователя с именем $nickname нет") {}