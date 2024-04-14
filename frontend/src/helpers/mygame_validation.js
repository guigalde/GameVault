export function validateMyGame(personalVideogame){

    const errors = {
        timePlayedError: "",
        markError: "",
        completionTimeError: "",
        completedOnError: "",
        acquiredOnError: "",
        platformError: ""
    };
    if(personalVideogame.timePlayed === null){
        errors.timePlayedError = "Time played cannot be empty";
    }else if(personalVideogame.timePlayed < 0){
        errors.timePlayedError = "Time played cannot be negative";
    }

    if( personalVideogame.mark === null){
        errors.markError = "Mark cannot be empty";
    }else if(personalVideogame.mark < 0 || personalVideogame.mark > 10){
        errors.markError = "Mark must be between 0 and 10";
    }else if(typeof personalVideogame.mark === 'string'){
        errors.markError = "Mark must be a number";
    }

    if(personalVideogame.completionTime < 0){
        errors.completionTimeError = "Completion time cannot be negative";
    }else if(typeof personalVideogame.completionTime === 'string'){
        errors.completionTimeError = "Completion time must be a number";
    }
    const dateFormatRegex = /^\d{4}-\d{2}-\d{2}$/;
    if(personalVideogame.completedOn!== null && !dateFormatRegex.test(personalVideogame.completedOn)){
        errors.completedOnError = "Wrong date format";
    }

    if(personalVideogame.acquiredOn!== null && !dateFormatRegex.test(personalVideogame.acquiredOn)){
        errors.acquiredOnError = "Wrong date format";
    }

    if(personalVideogame.platform === ""){
        errors.platformError = "Platform cannot be empty";
    }

    return errors;
}