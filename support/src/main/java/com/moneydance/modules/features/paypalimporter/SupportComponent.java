package com.moneydance.modules.features.paypalimporter;

import com.moneydance.apps.md.controller.FeatureModuleContext;
import com.moneydance.modules.features.paypalimporter.controller.ViewController;
import com.moneydance.modules.features.paypalimporter.domain.DateConverter;
import com.moneydance.modules.features.paypalimporter.model.AccountFilter;
import com.moneydance.modules.features.paypalimporter.model.IAccountBook;
import com.moneydance.modules.features.paypalimporter.service.ServiceProvider;
import com.moneydance.modules.features.paypalimporter.util.Localizable;
import com.moneydance.modules.features.paypalimporter.util.Preferences;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import javax.inject.Singleton;

import dagger.Component;

/**
 * @author Florian J. Breunig
 */
@Component(modules = {
        SupportModule.class,
})
@Singleton
public interface SupportComponent {

    Settings settings();

    Preferences preferences();

    Localizable localizable();

    FeatureModuleContext context();

    ViewController viewController();

    DateConverter dateConverter();

    ServiceProvider serviceProvider();

    IAccountBook accountBook();

    AccountFilter accountFilter();
}
