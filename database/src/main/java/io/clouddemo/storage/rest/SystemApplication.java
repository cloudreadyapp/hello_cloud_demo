/*******************************************************************************
 * Copyright (c) 2020, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * IBM Corporation - initial API and implementation
 *******************************************************************************/

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
