package bll.exceptions

class InvalidArgumentException(message: String, val argument: String? = null): Exception(message) {}