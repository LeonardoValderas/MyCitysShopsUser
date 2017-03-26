package com.valdroide.mycitysshopsuser.main.offer;


public class OfferActivityInteractorImpl implements OfferActivityInteractor {

    private OfferActivityRepository repository;

    public OfferActivityInteractorImpl(OfferActivityRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getListOffer(int id_shop) {
        repository.getListOffer(id_shop);
    }
}
