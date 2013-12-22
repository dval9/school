/** TOM CROWFOOT
 *  10037477
 *  CPSC 418 ASSIGNMENT 4
 */

import java.io.*;
import java.math.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;

/**
 * This class provides an implementation of 1024-bit RSA-OAEP.
 *
 * @author Mike Jacobson
 * @version 1.0, October 23, 2013
 */
public class RSATool {
    // OAEP constants
    private final static int K = 128;   // size of RSA modulus in bytes
    private final static int K0 = 16;  // K0 in bytes
    private final static int K1 = K-K0-16;  // K1 in bytes

    // RSA key data
    private BigInteger n;
    private BigInteger e, d, p, q;

    //    Chinese Remainder decryption as described in Problem 3
	private BigInteger dp, dq, mp, mq, x, y;
    // SecureRandom for OAEP and key generation
    private SecureRandom rnd;

    private boolean debug = false;
	
	private int bitlength = 1536;
	private int certanty = 10;



    /**
     * Utility for printing protocol messages
     * @param s protocol message to be printed
     */
    private void debug(String s) {
	if(debug) 
	    System.out.println("Debug RSA: " + s);
    }


    /**
     * G(M) = 1st K-K0 bytes of successive applications of SHA1 to M
     */
    private byte[] G(byte[] M) {
        MessageDigest sha1 = null;
	try {
	    sha1 = MessageDigest.getInstance("SHA1");
	}
	catch (NoSuchAlgorithmException e) {
	    System.out.println(e);
	    System.exit(1);
	}


	byte[] output = new byte[K-K0];
	byte[] input = M;

	int numBytes = 0;
	while (numBytes < K-K0) {
          byte[] hashval = sha1.digest(input);

	  if (numBytes + 20 < K-K0)
	      System.arraycopy(hashval,0,output,numBytes,K0);
	  else
	      System.arraycopy(hashval,0,output,numBytes,K-K0-numBytes);

	  numBytes += 20;
	  input = hashval;
	}

	return output;
    }



    /**
     * H(M) = the 1st K0 bytes of SHA1(M)
     */
    private byte[] H(byte[] M) {
        MessageDigest sha1 = null;
	try {
	    sha1 = MessageDigest.getInstance("SHA1");
	}
	catch (NoSuchAlgorithmException e) {
	    System.out.println(e);
	    System.exit(1);
	}

        byte[] hashval = sha1.digest(M);
 
	byte[] output = new byte[K0];
	System.arraycopy(hashval,0,output,0,K0);

	return output;
    }

	// generate a strong prime using gordon's algorithm, found Handbook of Applied Cryptography pg 150 algorithm 4.53
	private BigInteger genStrongPrime(int length, int cert){
		BigInteger TWO = new BigInteger("2");
		BigInteger i = BigInteger.ONE;
		BigInteger j = BigInteger.ONE;
		BigInteger s = new BigInteger(length/2, cert, rnd);
		BigInteger t = new BigInteger(length/2, cert, rnd);
		BigInteger r;
		while(true){
			r = BigInteger.ONE;
			r = r.multiply(TWO);
			r = r.multiply(i);
			r = r.multiply(t);
			r = r.add(BigInteger.ONE);
			if(r.isProbablePrime(cert))
				break;
			i = i.add(BigInteger.ONE);
		}
		BigInteger p0 = s.modPow(r.subtract(TWO),r);
		p0 = p0.multiply(TWO);
		p0 = p0.multiply(s);
		p0 = p0.subtract(BigInteger.ONE);
		BigInteger p;
		while(true){
			p = BigInteger.ONE;
			p = p.multiply(TWO);
			p = p.multiply(j);
			p = p.multiply(r);
			p = p.multiply(s);
			p = p.add(p0);
			if(p.isProbablePrime(cert))
				break;
			j = j.add(BigInteger.ONE);
		}
		return p;
	}

    /**
     * Construct instance for decryption.  Generates both public and private key data.
     *
     *   Include whatever extra data is required to implement Chinese Remainder
     *   decryption as described in Problem 3.
     */
    public RSATool(boolean setDebug) {
	// set the debug flag
	debug = setDebug;

	rnd = new SecureRandom();
	// requirements for good p and q:
	// must be prime DONE
	// large atleast 2^1536 DONE
	// abs(p-q) distance > 2^128 DONE
	// p-1,q-1,p+1,q+1 must have large prime factor, see genStrongPrime DONE
	// p/q should not be near the ratio of two small integers ???
	// generate strong p and q
	// d > n^.292 DONE
	debug("Computing strong prime p and q.");
	while(true){
		p = genStrongPrime(bitlength,certanty);
		q = genStrongPrime(bitlength,certanty);
		BigInteger psq = p.subtract(q);
		psq = psq.abs();
		BigInteger two128 = (BigInteger.ONE).shiftLeft(128);
		if(psq.compareTo(two128) > 0)
			break;
	}
	debug("Got p = "+p.toString());
	debug("Got q = "+q.toString());
	n = p.multiply(q);
	debug("Using n = "+n.toString());
	BigInteger p1 = p.subtract(BigInteger.ONE);
	BigInteger q1 = q.subtract(BigInteger.ONE);
	BigInteger phin = p1.multiply(q1);
	debug("phi(n) = "+phin.toString());
	while(true){
		e = new BigInteger("65537");
		d = e.modInverse(phin);		
		BigInteger n73 = n;
		n73 = n73.pow(73);
		BigInteger d250 = d;
		d250 = d250.pow(250);
		if(d250.compareTo(n73) > 0)
			break;
		else
			e = e.nextProbablePrime();
	}	
	debug("e = "+e.toString());
	debug("d = "+d.toString());
	dp = d.mod(p1);
	debug("d mod p-1 = "+dp.toString());
	dq = d.mod(q1);
	debug("d mod q-1 = "+dq.toString());
	x = p.modInverse(q);
	debug("p^-1 mod q = "+x.toString());
	y = q.modInverse(p);
	debug("q^-1 mod p = "+y.toString());
    }


    /**
     * Construct instance for encryption, with n and e supplied as parameters.  No
     * key generation is performed - assuming that only a public key is loaded
     * for encryption.
     */
    public RSATool(BigInteger new_n, BigInteger new_e, boolean setDebug) {
	// set the debug flag
	debug = setDebug;

	// initialize random number generator
	rnd = new SecureRandom();

	n = new_n;
	e = new_e;
	d = p = q = null;

    }



    public BigInteger get_n() {
	return n;
    }

    public BigInteger get_e() {
	return e;
    }



    /**
     * Encrypts the given byte array using RSA-OAEP.
     *
     *
     * @param plaintext  byte array representing the plaintext
     * @throw IllegalArgumentException if the plaintext is longer than K-K0-K1 bytes
     * @return resulting ciphertext
     */
    public byte[] encrypt(byte[] plaintext) {
	debug("In RSA encrypt");

	// make sure plaintext fits into one block
	if (plaintext.length > K-K0-K1)
	    throw new IllegalArgumentException("plaintext longer than one block");

	debug("Applying OAEP padding");
	BigInteger st;
	byte[] zeros = new byte[K1];
	for(int i=0;i<zeros.length;i++)
		zeros[i]=0;
	byte[] plaintext2 = new byte[plaintext.length+zeros.length];
	System.arraycopy(plaintext, 0, plaintext2, 0, plaintext.length);
	System.arraycopy(zeros, 0, plaintext2, plaintext.length, zeros.length);
	BigInteger mk1 = new BigInteger(plaintext2);
	BigInteger r, gr, s, hs, t;
	while(true){
		r = new BigInteger(K0*8,rnd);
		gr = new BigInteger(G(r.toByteArray()));
		s = mk1.xor(gr);
		hs = new BigInteger(H(s.toByteArray()));
		t = r.xor(hs);
		byte[] tbyteform = t.toByteArray();
		byte[] sbyteform = s.toByteArray();
		byte[] stbyteform = new byte[sbyteform.length + tbyteform.length];
		System.arraycopy(sbyteform, 0, stbyteform, 0, sbyteform.length);
		System.arraycopy(tbyteform, 0, stbyteform, sbyteform.length, tbyteform.length);
		st = new BigInteger(stbyteform);
		if((st.compareTo(BigInteger.ZERO) > 0) && (st.compareTo(n) < 0) && (r.toByteArray().length == 16))
			break;
	}
	debug("r = "+CryptoUtilities.toHexString(r.toByteArray()));
	debug("G(r) = "+CryptoUtilities.toHexString(gr.toByteArray()));
	debug("padded plaintext = "+CryptoUtilities.toHexString(mk1.toByteArray()));
	debug("s = "+CryptoUtilities.toHexString(s.toByteArray()));
	debug("H(s) = "+CryptoUtilities.toHexString(hs.toByteArray()));
	debug("t = "+CryptoUtilities.toHexString(t.toByteArray()));
	debug("s||t = "+CryptoUtilities.toHexString(st.toByteArray()));
	debug("M = "+st.toString());
	debug("Encrypting M");
	BigInteger cipher = st.modPow(e, n);
	debug("C = "+cipher.toString());
	return cipher.toByteArray();
    }


    /**
     * Decrypts the given byte array using RSA.
     *
     *
     * @param ciphertext  byte array representing the ciphertext
     * @throw IllegalArgumentException if the ciphertext converst to an integer greater than n
     * @throw IllegalStateException if the class is not initialized for decryption
     * @return resulting plaintexttext
     */
    public byte[] decrypt(byte[] ciphertext) {
	debug("In RSA decrypt");

	// make sure class is initialized for decryption
	if (d == null)
	    throw new IllegalStateException("RSA class not initialized for decryption");

	BigInteger c = new BigInteger(ciphertext);
	debug("Decrypting C = "+ c.toString());
	mp = c.modPow(dp, p);
	mq = c.modPow(dq, q);
	mp = mp.multiply(y);
	mp = mp.multiply(q);
	mq = mq.multiply(x);
	mq = mq.multiply(p);
	BigInteger m = mq.add(mp);
	m = m.mod(n);
	debug("M = "+ m.toString());
	debug("Removing OAEP");
	byte[] mbyte = m.toByteArray();
	debug("s||t = "+CryptoUtilities.toHexString(m.toByteArray()));
	byte[] sbyte = new byte[K-K0];
	System.arraycopy(mbyte, 0, sbyte, 0, sbyte.length);
	byte[] tbyte = new byte[K0];
	System.arraycopy(mbyte, sbyte.length, tbyte, 0, tbyte.length);
	BigInteger s = new BigInteger(sbyte);
	debug("s = "+CryptoUtilities.toHexString(s.toByteArray()));
	BigInteger t = new BigInteger(tbyte);
	debug("t = "+CryptoUtilities.toHexString(t.toByteArray()));
	BigInteger hs = new BigInteger(H(sbyte));
	debug("H(s) = "+CryptoUtilities.toHexString(hs.toByteArray()));
	BigInteger r = t.xor(hs);
	debug("r = "+CryptoUtilities.toHexString(r.toByteArray()));
	BigInteger gr = new BigInteger(G(r.toByteArray()));
	debug("G(r) = "+CryptoUtilities.toHexString(gr.toByteArray()));
	m = s.xor(gr);
	debug("M = "+CryptoUtilities.toHexString(m.toByteArray()));
	debug("M as integer = "+m.toString());
	return m.toByteArray();
    }
}































