/*
 * Copyright 2014-2018 Chronicle Software
 *
 * http://chronicle.software
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
