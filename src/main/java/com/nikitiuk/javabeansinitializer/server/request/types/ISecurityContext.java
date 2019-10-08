package com.nikitiuk.javabeansinitializer.server.request.types;

import java.security.Principal;

public interface ISecurityContext {

    Principal getUserPrincipal();

    boolean isUserInRole(String role);
}