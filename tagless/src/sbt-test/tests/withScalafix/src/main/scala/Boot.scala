import com.dwolla.test.TestService

object Boot {
  type Service[F[_]] = TestService.TestService[F]
}
