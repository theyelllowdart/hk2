package org.jvnet.hk2.guice.bridge.test;

import org.jvnet.hk2.annotations.Service;

import javax.inject.Inject;
import javax.inject.Named;

@Service
public class HK2ServiceNamedInjection {
    @Inject
    @Named("color")
    public String colorNamedInjection;
}
