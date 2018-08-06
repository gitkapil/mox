# DRAGON TEST HARNESS
=======================

Closed Dragon is a set of open APIs which are basically payment services and would be used by the third party merchants and their technical team to integrate within their apps or websites.

## How to Run
---------------

verify -P<profile> -Denv=<Env Name> -Dversion=v<version number>

<i>Example: </i>

To run functional tests on playpen/dev env (v1.0 is the default version)

command: verify -Pfunctional -Denv=playpen -Dversion=v1.0


##PreRequisites
-----------------

- Every env should have a config file under test/ resources
- Every config file should have a valid merchant (combination of merchnat id & secret)
- Every config file should have a valid application ID registered on Azure tenant
- Every config file should have the jwks_uri