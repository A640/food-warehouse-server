package foodwarehouse.web.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import foodwarehouse.core.data.user.Account;
import foodwarehouse.core.data.employee.EmployeePersonalData;

public record EmployeeRequest(
        @JsonProperty("account") Account account,
        @JsonProperty("personal_data") EmployeePersonalData employeePersonalData) {
}
