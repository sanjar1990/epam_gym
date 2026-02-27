package com.epam.gym.specification;

import com.epam.gym.dto.GetTraineeTrainingsCriteriaFilterDTO;
import com.epam.gym.dto.GetTrainerTrainingsCriteriaFilterDTO;
import com.epam.gym.entity.Training;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public class TrainingSpecification {

    public static Specification<Training> filterByCriteriaForTrainee(GetTraineeTrainingsCriteriaFilterDTO dto) {
        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            Join<Object, Object> traineeJoin = root.join("trainee");
            Join<Object, Object> traineeUserJoin = traineeJoin.join("user");
            predicate = cb.and(predicate,
                    cb.equal(traineeUserJoin.get("username"), dto.getTraineeUsername())
            );

            if (dto.getFromDate() != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("trainingDate"), dto.getFromDate()));
            }

            if (dto.getToDate() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(
                                root.get("trainingDate"), dto.getToDate()));
            }

            if (dto.getTrainerName() != null && !dto.getTrainerName().isBlank()) {
                Join<Object, Object> trainerJoin = root.join("trainer");
                Join<Object, Object> trainerUserJoin = trainerJoin.join("user");

                Expression<String> fullName = cb.concat(
                        cb.concat(trainerUserJoin.get("firstName"), " "),
                        trainerUserJoin.get("lastName")
                );

                predicate = cb.and(predicate,
                        cb.like(
                                cb.lower(fullName),
                                "%" + dto.getTrainerName().toLowerCase() + "%"
                        ));
            }

            if (dto.getTrainingType() != null && !dto.getTrainingType().isBlank()) {
                Join<Object, Object> typeJoin = root.join("trainingType");

                predicate = cb.and(predicate,
                        cb.equal(typeJoin.get("trainingTypeName"), dto.getTrainingType())
                );
            }

            return predicate;
        };
    }

    public static Specification<Training> filterByCriteriaForTrainer(GetTrainerTrainingsCriteriaFilterDTO dto) {
        return (root, query, cb) -> {

            Predicate predicate = cb.conjunction();

            Join<Object, Object> trainerJoin = root.join("trainer");
            Join<Object, Object> trainerUserJoin = trainerJoin.join("user");

            predicate = cb.and(predicate,
                    cb.equal(
                            trainerUserJoin.get("username"),
                            dto.getTrainerUsername()
                    )
            );

            if (dto.getFromDate() != null) {
                predicate = cb.and(predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("trainingDate"), dto.getFromDate()));
            }

            if (dto.getToDate() != null) {
                predicate = cb.and(predicate,
                        cb.lessThanOrEqualTo(
                                root.get("trainingDate"), dto.getToDate()));
            }

            if (dto.getTraineeName() != null && !dto.getTraineeName().isBlank()) {

                Join<Object, Object> traineeJoin = root.join("trainee");
                Join<Object, Object> traineeUserJoin = traineeJoin.join("user");

                Expression<String> fullName = cb.concat(
                        cb.concat(traineeUserJoin.get("firstName"), " "),
                        traineeUserJoin.get("lastName")
                );

                predicate = cb.and(predicate,
                        cb.like(
                                cb.lower(fullName),
                                "%" + dto.getTraineeName().toLowerCase() + "%"
                        )
                );
            }

            if (dto.getTrainingType() != null && !dto.getTrainingType().isBlank()) {
                Join<Object, Object> typeJoin = root.join("trainingType");

                predicate = cb.and(predicate,
                        cb.equal(typeJoin.get("trainingTypeName"), dto.getTrainingType())
                );
            }

            return predicate;
        };
    }
}