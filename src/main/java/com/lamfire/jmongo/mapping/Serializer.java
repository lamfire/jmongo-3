package com.lamfire.jmongo.mapping;


import org.bson.types.Binary;

import java.io.*;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;



public final class Serializer {
    private Serializer() {
    }


    public static byte[] serialize(final Object o, final boolean zip) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        OutputStream os = baos;
        ObjectOutputStream oos = null;
        try {
            if (zip) {
                os = new GZIPOutputStream(os);
            }
            oos = new ObjectOutputStream(os);
            oos.writeObject(o);
            oos.flush();
        } finally {
            if (oos != null) {
                oos.close();
            }
            os.close();
        }

        return baos.toByteArray();
    }


    public static Object deserialize(final Object data, final boolean zipped) throws IOException, ClassNotFoundException {
        final ByteArrayInputStream bais;
        if (data instanceof Binary) {
            bais = new ByteArrayInputStream(((Binary) data).getData());
        } else {
            bais = new ByteArrayInputStream((byte[]) data);
        }

        InputStream is = bais;
        try {
            if (zipped) {
                is = new GZIPInputStream(is);
            }

            final ObjectInputStream ois = new ObjectInputStream(is);
            return ois.readObject();
        } finally {
            is.close();
        }
    }

}
