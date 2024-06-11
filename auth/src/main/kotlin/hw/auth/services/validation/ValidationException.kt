package hw.auth.services.validation

class ValidationException(message: String, val argumentName: String): Exception(message) {}