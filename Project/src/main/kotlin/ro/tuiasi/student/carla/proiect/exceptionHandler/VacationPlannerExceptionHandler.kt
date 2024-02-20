package ro.tuiasi.student.carla.proiect.exceptionHandler

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpClientErrorException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.lang.Exception

@ControllerAdvice
class VacationPlannerExceptionHandler : ResponseEntityExceptionHandler() {
    private val log: Logger = LoggerFactory.getLogger(VacationPlannerExceptionHandler::class.java)

    @ExceptionHandler(HttpClientErrorException::class)
    fun handleHttpClientErrorException(
        ex: HttpClientErrorException
    ): ResponseEntity<ErrorMessage>  {
        val message = ErrorMessage(
            ex.statusCode.value(),
            "Http error: ${ex.message}",
            "",
        )
        when (ex.statusCode) {
            HttpStatus.NOT_FOUND,
            HttpStatus.NO_CONTENT -> log.info(message.message)

            HttpStatus.UNAUTHORIZED -> log.warn(message.message)

            HttpStatus.FORBIDDEN,
            HttpStatus.BAD_REQUEST,
            HttpStatus.UNPROCESSABLE_ENTITY,
            HttpStatus.CONFLICT,
            HttpStatus.INTERNAL_SERVER_ERROR -> log.error(message.message)

            else -> log.error(message.message)
        }
        return ResponseEntity<ErrorMessage>(message, ex.statusCode)
    }

    @ExceptionHandler(Exception::class)
    fun handleException(
        ex: Exception
    ): ResponseEntity<ErrorMessage> {
        val message = ErrorMessage(
            HttpStatus.BAD_REQUEST.value(),
            "Error: ${ex.localizedMessage}",
            "",
        )
        log.error(message.message)
        return ResponseEntity<ErrorMessage>(message, HttpStatus.BAD_REQUEST)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val message = ErrorMessage(
            status.value(),
            "Validation error: ${ex.message}",
            request.getDescription(false),
        )
        log.warn(message.message)
        return super.handleExceptionInternal(ex, message, headers, status, request)
    }

    override fun handleExceptionInternal(
        ex: Exception,
        body: Any?,
        headers: HttpHeaders,
        statusCode: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        val message = ErrorMessage(
            statusCode.value(),
            "Unknown error: ${ex.message}",
            request.getDescription(false),
        )
        log.error(message.message)
        return super.handleExceptionInternal(ex, message, headers, statusCode, request)
    }
}