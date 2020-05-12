package us.master.entregable1;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import us.master.entregable1.entity.Trip;

public class FirestoreService {
    private static String userId;
    private static FirebaseFirestore mDatabase;
    private static FirestoreService service;

    public static FirestoreService getServiceInstance() {
        if (service == null || mDatabase == null) {
            mDatabase = FirebaseFirestore.getInstance();
            service = new FirestoreService();
        }
        if (userId == null || userId.isEmpty()) {
            userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }
        return service;
    }

    public void saveTravel(Trip travel, OnCompleteListener<DocumentReference> listener) {
        mDatabase.collection("users").document(userId).collection("travel").add(travel).addOnCompleteListener(listener);
    }

    public void getTravels(OnCompleteListener<QuerySnapshot> querySnapshotOnCompleteListener) {
        mDatabase.collection("users").document(userId).collection("travel").get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public void getTravelsFiltered(OnCompleteListener<QuerySnapshot> querySnapshotOnCompleteListener) {
        mDatabase.collection("users").document(userId).collection("travel")
                // .orderBy("end")
                .whereGreaterThanOrEqualTo("rate",9.5)
                .orderBy("rate", Query.Direction.DESCENDING)
                .limit(3)
                .get().addOnCompleteListener(querySnapshotOnCompleteListener);
    }

    public void getTravel(String id, EventListener<DocumentSnapshot> snapshotListener) {
        mDatabase.collection("users").document(userId).collection("travel").document(id).addSnapshotListener(snapshotListener);
    }

    public void deleteTravel(String id, EventListener<DocumentSnapshot> snapshotListener) {
        mDatabase.collection("users").document(userId).collection("travel").document(id).delete();
    }

    public ListenerRegistration getTravels(EventListener<QuerySnapshot> querySnapshotEventListener) {
        return mDatabase.collection("users").document(userId).collection("travel").addSnapshotListener(querySnapshotEventListener);
    }
}
