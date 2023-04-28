namespace java com.dwolla.test
namespace csharp Dwolla.Test

struct TestRequest {
	1: required string text,
	2: optional string moretext
}

struct TestResponse {
	1: string cipher
}

service TestService {
    TestResponse Test(1:required TestRequest request)
}
