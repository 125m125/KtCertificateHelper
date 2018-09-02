package de._125m125.kt.certificateHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;

public enum Permission {
    READ_ITEMLIST_PERMISSION(0b00000000000000000000000000000001),
    READ_ORDERS_PERMISSION(0b00000000000000000000000000000010),
    READ_PAYOUTS_PERMISSION(0b00000000000000000000000000000100),
    READ_MESSAGES_PERMISSION(0b00000000000000000000000000001000),

    WRITE_ORDERS_PERMISSION(0b01000000000000000000000000000000),
    WRITE_PAYOUTS_PERMISSION(0b00100000000000000000000000000000),

    TWO_FA_PERMISSION(0b00000000000000001000000000000000),;

    private static ASN1ObjectIdentifier          base = new ASN1ObjectIdentifier(
            "2.25.185209158787762157444538593016840947074");

    private static final Map<String, Permission> OID_TO_PERMISSION;

    static {
        final List<Permission> allPerms = new ArrayList<>();
        OID_TO_PERMISSION = new HashMap<>();
        for (final Permission p : Permission.values()) {
            allPerms.add(p);
            final String oid = Permission.base.branch(Integer.toString(p.getInteger())).getId();
            p.oid = oid;
            Permission.OID_TO_PERMISSION.put(oid, p);
        }
    }

    public static Permission ofOID(final String oid) {
        return Permission.OID_TO_PERMISSION.get(oid);
    }

    private final int integer;
    private String    oid;

    Permission(final int integer) {
        this.integer = integer;
    }

    public boolean isPresentIn(final int permissions) {
        return (permissions & this.integer) == this.integer;
    }

    public int getInteger() {
        return this.integer;
    }

    public int appendTo(final int current) {
        return current | this.integer;
    }

    public String getOid() {
        return this.oid;
    }

}