Route {
    var <in;
    var <>out;
    var <>amp = 1;
    var <>numCh = 2;
    var <>numFx = 2;
    var <>fxBus, <>fxlvl;
    var <>server;
    var <>da = 2;
    var sig, env, gate=1, route;

    *new { arg out, amp, numFx, fxBus, fxlvl, server;
        ^super.newCopyargs( out, amp, numFx, fxBus, fxlvl, server).init;
    }

    init {
        this.createRoute;
    }

    createRoute {
        route = {
            server = server ? Server.default;
            in = Bus.audio(server, numCh);
            out = Bus.audio(server, numCh);
            gate = 1;
            env = EnvGen.kr(Env.asr(0.1, amp, 1.0), gate: gate, doneAction: da);
            fxBus = Bus.audio(server, numCh) ! numFx;
            fxlvl = 0.0 ! numFx;
            sig = In.ar(in, numCh);
            sig = sig * env * amp;
            Out.ar(out, sig);
            numFx.do{|i|
                Out.ar(out, sig * fxlvl[i]);
            };
        }.play;
    }

    free {
        [in, out, route].do(_.free);
    }
}
