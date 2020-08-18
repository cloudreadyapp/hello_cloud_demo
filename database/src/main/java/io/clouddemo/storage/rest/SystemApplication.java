package io.clouddemo.storage.rest;

import javax.ws.rs.core.Application;
import javax.ws.rs.ApplicationPath;

// tag::applicationPath[]
@ApplicationPath("demo")
// end::applicationPath[]
// tag::systemApplication[]
public class SystemApplication extends Application {

}
// end::systemApplication[]
