
![release](https://github.com/aQute-os/v2Archive.osgi.enroute/workflows/release/badge.svg)

## Introduction

This is a clone of the v2Archive.osgi.enroute repository. It has been moved to the biz.aQute group
to prevent clashes, however, the packages and project names are not changed.

This repository will be maintained to fix bugs and update versions. However, the intention is to
move to all the interesting projects to [biz.aQute.osgi.util] in the bizQute namespace. Several
projects in the v2Archive enRoute have now become OSGi specifications.

We're currently building 2.3.0-SNAPSHOT.

## What's in It?

The enRoute project experimented with using bundles as the primary delivery vehicle instead of
making an OSGi solution one part of a bigger whole. For example, it experimented with webresources
being delivered in bundles. This works very well for embedded projects and there were some
really innovative ideas that drove several OSGi specs.

The core idea was to provide a _base_ API that would provide the minimum environment for an
application. The v2Archive enRoute provided one or more implementations for each of these APIs.
Since this base API had a _distro_, things worked quite well out of the box.

At that time, OSGi was focusing heavily on IoT (does that term still gets used?). We added
a very interesting model to represent devices although with retrospect, it might have been
better to make this a separate base API.

## Why v2Archive?

Why v2Archive? Well, it is about of an 'honor' name :-) The [original enRoute][1] that was based on
a much less ambitious maven based system and enRoute 'classic' was abandoned. However, the website
and the github repos were archived. aQute has several customers that got a lot out of the v2Archive
enRoute work, it is still probably the easiest way to get into OSGi.

## Using v2Archive enRoute

You can use this repository in your project by adding the following plugin

    aQute.bnd.repository.maven.provider.MavenBndRepository;\
        name                =enRoute;\
        noupdateOnRelease   =true;\
        readOnly            =true;\
        source              ="${enRoute}"

    enRoute= \
        biz.aQute:osgi.enroute.websecurity.adapter:2.2.0, \
        biz.aQute:osgi.enroute.webconsole.xray.provider:2.2.0, \
        biz.aQute:osgi.enroute.web.simple.test:2.2.0, \
        biz.aQute:osgi.enroute.web.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.twitter.bootstrap.webresource:3.3.5, \
        biz.aQute:osgi.enroute.stackexchange.pagedown.webresource:1.1.1, \
        biz.aQute:osgi.enroute.scheduler.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.rest.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.logreader.rolling.provider:2.2.0, \
        biz.aQute:osgi.enroute.logger.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.junit.wrapper:4.13.0, \
        biz.aQute:osgi.enroute.jsplumb.webresource:1.7.6, \
        biz.aQute:osgi.enroute.jsonrpc.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.iot.pi.provider:2.2.0, \
        biz.aQute:osgi.enroute.iot.pi.command:2.2.0, \
        biz.aQute:osgi.enroute.iot.lego.adapter:2.2.0, \
        biz.aQute:osgi.enroute.iot.circuit.application:2.2.0, \
        biz.aQute:osgi.enroute.iot.circuit.provider:2.2.0, \
        biz.aQute:osgi.enroute.iot.circuit.command:2.2.0, \
        biz.aQute:osgi.enroute.hamcrest.wrapper:1.3.0, \
        biz.aQute:osgi.enroute.google.angular.webresource:1.5.7, \
        biz.aQute:osgi.enroute.github.angular-ui.webresource:0.13.3, \
        biz.aQute:osgi.enroute.executor.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.equinox.log.adapter:2.2.0, \
        biz.aQute:osgi.enroute.easse.simple.adapter:2.2.0, \
        biz.aQute:osgi.enroute.dtos.bndlib.provider:2.2.0, \
        biz.aQute:osgi.enroute.configurer.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.bostock.d3.webresource:3.5.6, \
        biz.aQute:osgi.enroute.bndtools.templates:2.2.0, \
        biz.aQute:osgi.enroute.base.test:2.2.0, \
        biz.aQute:osgi.enroute.base.provider:2.2.0, \
        biz.aQute:osgi.enroute.base.debug.provider:2.2.0, \
        biz.aQute:osgi.enroute.base.api:2.2.0, \
        biz.aQute:osgi.enroute.authorization.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.authenticator.simple.provider:2.2.0, \
        biz.aQute:osgi.enroute.authenticator.github.provider:2.2.0
        

## Contributing

Want to hack on osgi.enroute? See [CONTRIBUTING.md](CONTRIBUTING.md) for information on building, testing and contributing changes.

They are probably not perfect, please let us know if anything feels wrong or incomplete.

## License

The contents of this repository are made available to the public under the terms of the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
Bundles may depend on non Apache Licensed code.

[1]: https://v2archive.enroute.osgi.org/
