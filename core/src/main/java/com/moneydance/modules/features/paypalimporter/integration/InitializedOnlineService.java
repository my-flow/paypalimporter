package com.moneydance.modules.features.paypalimporter.integration;

import com.infinitekind.moneydance.model.AccountBook;
import com.infinitekind.moneydance.model.OnlineService;
import com.infinitekind.util.StreamTable;
import com.moneydance.modules.features.paypalimporter.util.Settings;

import java.util.Date;

/**
 * Use this class to initialize an <code>OnlineService</code>.
 */
final class InitializedOnlineService extends OnlineService {

    static final String KEY_SERVICE_TYPE = "type";

    InitializedOnlineService(
            final AccountBook book,
            final Settings settings,
            final Date dateUpdated) {
        super(book);
        init(settings, dateUpdated);
    }

    InitializedOnlineService(
            final AccountBook book,
            final StreamTable legacyInfo,
            final Settings settings,
            final Date dateUpdated) {
        super(book, legacyInfo);
        init(settings, dateUpdated);
    }

    private void init(final Settings settings, final Date dateUpdated) {
        setParameter(KEY_SERVICE_TYPE, settings.getServiceType());
        // see https://github.com/my-flow/paypalimporter/issues/9
        setParameter(ITEM_KEY_FI_TIK_ID, settings.getFITIKId());
        setFIId(settings.getFIId());
        setFIOrg(settings.getFIOrg());
        setFIName(settings.getFIName());
        setFIAddress1(settings.getFIAddress());
        setFICity(settings.getFICity());
        setFIState(settings.getFIState());
        setFIZip(settings.getFIZip());
        setFICountry(settings.getFICountry());
        setFIUrl(settings.getFIUrl());
        setDateUpdated(dateUpdated.getTime());
    }
}
