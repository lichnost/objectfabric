/**
 * This file is part of ObjectFabric (http://objectfabric.org).
 *
 * ObjectFabric is licensed under the Apache License, Version 2.0, the terms
 * of which may be found at http://www.apache.org/licenses/LICENSE-2.0.html.
 * 
 * Copyright ObjectFabric Inc.
 * 
 * This file is provided AS IS with NO WARRANTY OF ANY KIND, INCLUDING THE
 * WARRANTY OF DESIGN, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 */

package org.objectfabric;

/**
 * Manages handlers and caches needed by workspaces and servers to resolve URIs.
 */
public interface URIHandlersSet {

    URIHandler[] uriHandlers();

    void addURIHandler(URIHandler handler);

    void addURIHandler(int index, URIHandler handler);

    Location[] caches();

    void addCache(Location location);
}
