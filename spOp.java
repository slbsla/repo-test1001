public SepaSequenceDto findById(Long sequenceId) {
    CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
    CriteriaQuery<SepaSequenceDto> query = criteriaBuilder.createQuery(SepaSequenceDto.class);
    Root<SepaSequence> root = query.from(SepaSequence.class);

    // Join tables
    Join<SepaSequence, SepaSequenceStatus> statusJoin = root.join("status", JoinType.LEFT);
    Join<SepaSequence, SepaSignature> signatureJoin = root.join("signature", JoinType.LEFT);
    Join<SepaSequence, SepaSubSequence> subSequenceJoin = root.join("subSequence", JoinType.LEFT);

    // Select the fields for the DTO
    query.select(criteriaBuilder.construct(
        SepaSequenceDto.class,
        root.get("id"), // Assuming id is the primary key
        statusJoin.get("statusList"), // Assuming status has a list of statuses
        signatureJoin, // Assuming full signature object is needed
        root.get("currencyId"), // Adjust as needed based on the DTO field names
        root.get("sepaField"), // Adjust as needed
        root.get("sequenceDirection") // Adjust as needed
    ));

    // Add where clause for filtering by sequenceId
    query.where(criteriaBuilder.equal(root.get("id"), sequenceId));

    // Execute query and get a single result
    try {
        return entityManager.createQuery(query).getSingleResult();
    } catch (NoResultException e) {
        return null; // Or handle as needed
    }
}