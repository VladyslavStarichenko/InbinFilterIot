package nure.ua.iot.InbinFilterIot;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
public class WasteDto {
    LitterType litterType;
    Double amount;


    public LitterType getLitterType() {
        return litterType;
    }

    public void setLitterType(LitterType litterType) {
        this.litterType = litterType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
