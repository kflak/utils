Chain {
    var <>out;
    var <>numCh;
    var <>chain;
    var <>bus;
    var <group;

    *new { arg out, numCh, chain;
        ^super.newCopyArgs(out, numCh, chain).init;
    }

    init {
        this.createChain;
    }

    createChain {
        group = Group.new();
        bus = chain.size.collect{ Bus.audio(Server.default, numCh) };
        chain = chain.collect{|i, idx|
            case
            {idx == 0} { 
                if(i.class == Array){
                    i.collect{|j| 
                        Synth(j, [\in, bus[idx], \out, bus[idx + 1]], group, \addToHead) 
                    }
                }{
                    Synth(i, [\in, bus[idx], \out, bus[idx + 1]], group, \addToHead) 
                }
            }
            {idx == chain.size} { 
                if(i.class == Array){
                    i.collect{|j|
                        Synth(j, [\in, bus[idx], \out, out], group, \addToTail) 
                    }
                }{
                    Synth(i, [\in, bus[idx], \out, out], group, \addToTail)
                }
            }
            {idx > 0} { 
                if(i.class == Array){
                    i.collect{|j|
                        Synth(j, [\in, bus[idx], \out, bus[idx + 1]], group, \addToTail) 
                    }
                }{
                    Synth(i, [\in, bus[idx], \out, bus[idx + 1]], group, \addToTail) 
                }
            }
        }
    }

    free {
        group.free;
        bus.do(_.free);
    }
}
