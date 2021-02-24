package com.sg.ollie.dto;

import java.math.BigDecimal;
import java.util.Objects;

public class Tax {
    private String stateName;
    private String stateCode;
    private BigDecimal taxRate;

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tax tax = (Tax) o;
        return stateCode.equals(tax.stateCode) &&
                stateName.equals(tax.stateName) &&
                taxRate.equals(tax.taxRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(stateCode, stateName, taxRate);
    }
}
