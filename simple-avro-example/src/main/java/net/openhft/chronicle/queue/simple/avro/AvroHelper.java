/*
 * Copyright 2013-2025 chronicle.software; SPDX-License-Identifier: Apache-2.0
 */
package net.openhft.chronicle.queue.simple.avro;

import net.openhft.chronicle.core.Jvm;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class AvroHelper {

    private Schema schema;
    private DatumWriter<GenericRecord> datumWriter;
    private DatumReader<GenericRecord> datumReader;

    AvroHelper() {
        try {
            schema = new Schema.Parser().parse(getClass().getClassLoader().getResourceAsStream("user.avsc"));
            datumWriter = new GenericDatumWriter<>(schema);
            datumReader = new GenericDatumReader<>(schema);
        } catch (IOException ex) {
            Jvm.rethrow(ex);
        }
    }

    GenericRecord getGenericRecord() {
        return new GenericData.Record(schema);
    }

    void writeToOS(GenericRecord r, OutputStream os) throws IOException {
        BinaryEncoder encoder = EncoderFactory.get().directBinaryEncoder(os, null);
        datumWriter.write(r, encoder);
    }

    GenericRecord readFromIS(InputStream is) throws IOException {
        BinaryDecoder decoder = DecoderFactory.get().directBinaryDecoder(is, null);
        GenericRecord res = new GenericData.Record(schema);
        datumReader.read(res, decoder);
        return res;
    }

}
