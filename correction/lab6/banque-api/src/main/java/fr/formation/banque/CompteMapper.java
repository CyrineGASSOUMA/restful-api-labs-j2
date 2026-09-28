package fr.formation.banque;

public final class CompteMapper {

    private CompteMapper() { }

    public static CompteResponse toResponse(CompteEntity entity) {
        return new CompteResponse(entity.getId(), entity.getTitulaire(), entity.getType(), entity.getSolde());
    }

    public static CompteEntity toEntity(CompteRequest requete) {
        CompteEntity entity = new CompteEntity();
        entity.setTitulaire(requete.titulaire());
        entity.setType(requete.type());
        return entity;
    }
}
