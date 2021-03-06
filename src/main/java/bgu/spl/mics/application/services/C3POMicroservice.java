package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.FinishBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Ewoks;

/**
 * C3POMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 * @code Diary diary - a singleton used to record the time of the thread actions.
 */
public class C3POMicroservice extends MicroService {
    private final Diary diary = Diary.getDiary();

    public C3POMicroservice() {super("C3PO");
    }

    @Override
    /**
     * this method is called once when the event loop starts.
     * Used by the microservices to subscribes to the messages that it is interested to
     * receive, and to define a callback function to how it will handle it.
     */
    protected void initialize() {
        subscribeBroadcast(FinishBroadcast.class, c-> {
            terminate();
            diary.setC3POTerminate(System.currentTimeMillis());
        });

        subscribeEvent(AttackEvent.class, c-> {//Pram c: instance of type Message.
            // acquire the number of ewoks needed for the attack, simulate the attack by sleeping , release the ewoks and  complete the associated future for this event
            Ewoks ewoks = Ewoks.get();
            ewoks.acquireEwoks(c.getAttack().getSerials());
            try{
                Thread.sleep(c.getAttack().getDuration());
            }catch (InterruptedException ignored){}
            ewoks.releaseEwoks(c.getAttack().getSerials());
            complete(c,true);
            diary.addToTotalAttacks();
            diary.setC3POFinish(System.currentTimeMillis());
            });
    }
}
