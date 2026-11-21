package com.github.kwart.jd.sample;
public class DensePacked {
    public void a() { System.out.println("a"); }
    public void b() { System.out.println("b"); } class In1 { void x() { System.out.println("x"); } }
    public void c() { System.out.println("c"); } public void d() {
        System.out.println("d");
    }
    class In2 { void y() { System.out.println("y"); } } public void e() { System.out.println("e"); }
    private int f = 1; private int g = 2;
    DensePacked() { System.out.println("ctor"); } public void h() { System.out.println("h"); }
}
