// PayPal Importer for Moneydance - https://www.my-flow.com/paypalimporter/
// Copyright (C) 2013-2021 Florian J. Breunig. All rights reserved.

package com.moneydance.modules.features.paypalimporter.domain;

import com.moneydance.modules.features.paypalimporter.DaggerSupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportComponent;
import com.moneydance.modules.features.paypalimporter.SupportModule;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.Date;

import javax.swing.BoundedRangeModel;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Florian J. Breunig
 */
public final class DateConverterTest {

    private DateConverter dateConverter;

    @Before
    public void setUp() {
        SupportModule supportModule = new SupportModule();
        SupportComponent supportComponent = DaggerSupportComponent.builder().supportModule(supportModule).build();
        this.dateConverter = supportComponent.dateConverter();
    }

    @Test
    public void testGetBoundedRangeModel() {
        final Date startDate = new Date(Integer.MIN_VALUE);
        final Date valueDate = new Date(Integer.MAX_VALUE / 2);
        final Date endDate = new Date(Integer.MAX_VALUE);

        final BoundedRangeModel model = this.dateConverter.getBoundedRangeModel(
                startDate, endDate, valueDate);
        assertThat(model, notNullValue());
    }

    @Test
    public void testGetValidDate() {
        final Date validDate = this.dateConverter.getValidDate(new Date());
        assertThat(validDate, notNullValue());
    }
}
