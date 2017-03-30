/*
 * Copyright 2016-2017 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.n52.svalbard.encode;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

import java.io.ByteArrayOutputStream;
import java.util.Map;
import java.util.Set;

import net.opengis.swes.x20.DeleteSensorResponseDocument;

import org.apache.xmlbeans.XmlObject;
import org.junit.Test;

import org.n52.janmayen.http.MediaTypes;
import org.n52.shetland.ogc.sos.Sos2Constants;
import org.n52.shetland.ogc.sos.SosConstants;
import org.n52.shetland.ogc.sos.response.DeleteSensorResponse;
import org.n52.shetland.ogc.swes.SwesConstants;
import org.n52.shetland.w3c.SchemaLocation;
import org.n52.svalbard.encode.DeleteSensorResponseEncoder;
import org.n52.svalbard.encode.EncoderKey;
import org.n52.svalbard.encode.EncodingContext;
import org.n52.svalbard.encode.OperationResponseEncoderKey;
import org.n52.svalbard.encode.XmlEncoderKey;
import org.n52.svalbard.encode.exception.EncodingException;
import org.n52.svalbard.encode.exception.UnsupportedEncoderInputException;

import com.google.common.collect.Maps;

/**
 * TODO JavaDoc
 *
 * @author <a href="mailto:c.autermann@52north.org">Christian Autermann</a>
 *
 * @since 4.0.0
 */
@Deprecated
public class DeleteSensorResponseEncoderTest {

    @Test
    public void should_return_correct_encoder_keys() {
        Set<EncoderKey> returnedKeySet = new DeleteSensorResponseEncoder().getKeys();
        assertThat(returnedKeySet.size(), is(3));
        assertThat(returnedKeySet, hasItem(new XmlEncoderKey(SwesConstants.NS_SWES_20, DeleteSensorResponse.class)));
        assertThat(returnedKeySet, hasItem(new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.DeleteSensor, MediaTypes.TEXT_XML)));
        assertThat(returnedKeySet, hasItem(new OperationResponseEncoderKey(SosConstants.SOS, Sos2Constants.SERVICEVERSION,
                Sos2Constants.Operations.DeleteSensor, MediaTypes.APPLICATION_XML)));
    }

    @Test
    public void should_return_emptyMap_for_supportedTypes() {
        assertThat(new DeleteSensorResponseEncoder().getSupportedTypes(), is(not(nullValue())));
        assertThat(new DeleteSensorResponseEncoder().getSupportedTypes().isEmpty(), is(TRUE));
    }

    @Test
    public void should_return_emptySet_for_conformanceClasses() {
        assertThat(new DeleteSensorResponseEncoder().getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION), is(not(nullValue())));
        assertThat(new DeleteSensorResponseEncoder().getConformanceClasses(SosConstants.SOS, Sos2Constants.SERVICEVERSION).isEmpty(), is(TRUE));
    }

    @Test
    public void should_add_own_prefix_to_prefixMap() {
        Map<String, String> prefixMap = Maps.newHashMap();
        new DeleteSensorResponseEncoder().addNamespacePrefixToMap(prefixMap);
        assertThat(prefixMap.isEmpty(), is(FALSE));
        assertThat(prefixMap.containsKey(SwesConstants.NS_SWES_20), is(TRUE));
        assertThat(prefixMap.containsValue(SwesConstants.NS_SWES_PREFIX), is(TRUE));
    }

    @Test
    public void should_not_fail_if_prefixMap_is_null() {
        new DeleteSensorResponseEncoder().addNamespacePrefixToMap(null);
    }

    @Test
    public void should_return_contentType_xml() {
        assertThat(new DeleteSensorResponseEncoder().getContentType(), is(MediaTypes.TEXT_XML));
    }

    @Test
    public void should_return_correct_schema_location() {
        assertThat(new DeleteSensorResponseEncoder().getSchemaLocations().size(), is(1));
        SchemaLocation schemLoc = new DeleteSensorResponseEncoder().getSchemaLocations().iterator().next();
        assertThat(schemLoc.getNamespace(), is("http://www.opengis.net/swes/2.0"));
        assertThat(schemLoc.getSchemaFileUrl(), is("http://schemas.opengis.net/swes/2.0/swes.xsd"));
    }

    @Test(expected = UnsupportedEncoderInputException.class)
    public void should_return_exception_if_received_null() throws EncodingException {
        new DeleteSensorResponseEncoder().encode(null);
        new DeleteSensorResponseEncoder().encode(null, new ByteArrayOutputStream());
        new DeleteSensorResponseEncoder().encode(null, EncodingContext.empty());
    }

    @Test
    public void should_encode_DeleteSensor_response() throws EncodingException {
        final DeleteSensorResponse response = new DeleteSensorResponse();
        final String deletedProcedure = "deletedProcedure";
        response.setDeletedProcedure(deletedProcedure);
        final XmlObject encodedResponse = new DeleteSensorResponseEncoder().encode(response);
        assertThat(encodedResponse, is(instanceOf(DeleteSensorResponseDocument.class)));
        final DeleteSensorResponseDocument doc = (DeleteSensorResponseDocument) encodedResponse;
        assertThat(doc.isNil(), is(FALSE));
        assertThat(doc.getDeleteSensorResponse().getDeletedProcedure(), is(deletedProcedure));
        assertThat(doc.validate(), is(TRUE));
    }
}