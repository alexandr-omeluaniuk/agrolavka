package ss.martin.security.test.util;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ss.martin.security.model.RestResponse;

@RestController
@RequestMapping(TestConstants.PROTECTED_RESOURCE)
public class ProtectedRestController {
    
    @GetMapping
    public RestResponse get() {
        return new RestResponse(true, "Success response, Super!");
    }
}
