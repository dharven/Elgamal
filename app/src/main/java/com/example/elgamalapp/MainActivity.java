package com.example.elgamalapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText pv, alphav, mv, kv;
    TextView result1, result2;
    Button fin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fin = (Button) findViewById(R.id.button);
        result1 = findViewById(R.id.textView3);
        result2 = findViewById(R.id.textView4);
        fin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowResult();
            }
        });
    }

    @SuppressLint("ResourceType")
    public void ShowResult() {

        pv = (EditText) findViewById(R.id.editText);
        alphav = (EditText) findViewById(R.id.editText2);
        mv = (EditText) findViewById(R.id.editText3);
        kv = (EditText) findViewById(R.id.editText4);
        String Ep = pv.getText().toString();
        String Ealpha = alphav.getText().toString();
        String Em = mv.getText().toString();
        String Ek = kv.getText().toString();
        long p = Long.parseLong(Ep);
        long alpha = Long.parseLong(Ealpha);
        long m = Long.parseLong(Em);
        long k = Long.parseLong(Ek);
        signAlgo sign = new signAlgo(p, alpha, m, k);
        verify v = new verify(sign.p, sign.alpha, sign.beta, sign.m, sign.r, sign.s);
        v.verified();
    }

    class signAlgo {

        public long p, alpha, beta, m, r, s, k;

        private long z = 16;

        signAlgo(long a, long b, long c, long d) {
            p = a;
            alpha = b;
            beta = ((long) Math.pow(alpha, z)) % p;
            m = c;
            k = d;//createK();
            r = createR(alpha, k);
            s = createS();
        }
        long gcd(long a, long b) {
            if (a < b)
                return gcd(b, a);
            else if (a % b == 0)
                return b;
            else
                return gcd(b, a % b);
        }

	/*long createK() {
		long a = 2*(p-1);
		while(gcd(a,p-1)!=1) {
			a = (long) (Math.random()*(p-1));
		}
		return a;
	}*/

        long invK() {
            for (int x = 1; x < p - 1; x++)
                if ((k * x) % (p - 1) == 1)
                    return x;
            return 1;
        }

        long createR(long b, long c) {
            long a = ((long) Math.pow(b, c));
            if (a < (long) Math.pow(2, 36) - 1)
                return a % p;
            else//(a==(long)Math.pow(2, 36)-1)
                return (createR(b, c / 2) * createR(b, c - c / 2)) % p;
        }

        long createS() {
            long a = (invK() * (m - z * r)) % (p - 1);
            if (a >= 0)
                return a;
            else
                return (a + p - 1);
        }

    }

    class verify {

        public long p, alpha, beta, m, r, s;

        verify(long a, long b, long c, long d, long e, long f) {
            p = a;
            alpha = b;
            beta = c;
            m = d;
            r = e;
            s = f;
        }

	/*long v1() {
		return ((((long)Math.pow(beta,r))*((long)(Math.pow(r, s))))%p);
	}*/

        long v1(long b, long c, long d, long e) {
            long a = (((long) Math.pow(b, c)) * ((long) (Math.pow(d, e))));
            if (a < (long) Math.pow(2, 36) - 1)
                return a % p;
            else//(a==(long)Math.pow(2, 36)-1)
                return (v1(b, c / 2, d, e / 2) * v1(b, c - c / 2, d, e - e / 2)) % p;
        }

        long v2() {
            return (((long) Math.pow(alpha, m)) % p);
        }

        long v2(long b, long c) {
            long a = ((long) Math.pow(b, c));
            if (a < (long) Math.pow(2, 36) - 1)
                return a % p;
            else//(a==(long)Math.pow(2, 36)-1)
                return (v2(b, c / 2) * v2(b, c - c / 2)) % p;
        }

        @SuppressLint("SetTextI18n")
        void verified() {
            if (v1(beta, r, r, s) == v2(alpha, m)) {
                Toast.makeText(getApplicationContext(), "Signature verified using Elgamal.", Toast.LENGTH_LONG).show();
                long sp = v1(beta, r, r, s);
                long spp = v2(alpha, m);
                result1.setText("The value of v1 mod p: "+sp);
                result2.setText("The value of v2 mod p: "+spp);
            } else {
                Toast.makeText(getApplicationContext(), "Signature mismatch", Toast.LENGTH_LONG).show();
                long vp = v1(beta, r, r, s);
                long vpp = v2(alpha, m);
                result1.setText("The value of v1 mod p: "+ vp);
                result2.setText("The value of v2 mod p: "+ vpp);
            }
        }
    }
}




