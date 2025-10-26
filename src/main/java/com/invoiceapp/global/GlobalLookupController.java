// src/main/java/com/invoiceapp/global/GlobalLookupController.java
package com.invoiceapp.global;

import com.invoiceapp.global.dto.EnumOption;
import org.springframework.web.bind.annotation.*;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/lookups")
public class GlobalLookupController {
    @GetMapping("/mydata-payment-methods")
    public List<EnumOption> myDataPaymentMethods() {
        return Arrays.stream(MyDataPaymentMethod.values())
                .map(v -> new EnumOption(v.getCode(), v.getLabelEl()))
                .toList();
    }
}
