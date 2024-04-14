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
    }else if(Number(personalVideogame.timePlayed) < 0){
        errors.timePlayedError = "Time played cannot be negative";
    }

    if( personalVideogame.mark === null){
        errors.markError = "Mark cannot be empty";
    }else if(Number(personalVideogame.mark) < 0 || Number(personalVideogame.mark) > 10){
        errors.markError = "Mark must be between 0 and 10";
    }

    if(personalVideogame.completionTime < 0){
        errors.completionTimeError = "Completion time cannot be negative";
    }else if(Number(personalVideogame.completionTime) > Number(personalVideogame.timePlayed)){
        errors.completionTimeError = "Completion time cant be greater than time played";
    }
    
    const dateFormatRegex = /^\d{4}-\d{2}-\d{2}$/;
    if(personalVideogame.completedOn!== null && !dateFormatRegex.test(personalVideogame.completedOn)){
        errors.completedOnError = "Wrong date format";
    }else if(personalVideogame.completedOn!== null && personalVideogame.completedOn > new Date().toISOString().split('T')[0]){
        errors.completedOnError = "Date cannot be in the future";
    }else if(personalVideogame.completedOn!== null && personalVideogame.completedOn < personalVideogame.acquiredOn){
        errors.completedOnError = "Date cannot be before acquired date";
    }

    if(personalVideogame.acquiredOn!== null && !dateFormatRegex.test(personalVideogame.acquiredOn)){
        errors.acquiredOnError = "Wrong date format";
    }else if(personalVideogame.acquiredOn!== null && personalVideogame.acquiredOn > new Date().toISOString().split('T')[0]){
        errors.acquiredOnError = "Date cannot be in the future";
    }

    if(personalVideogame.platform === ""){
        errors.platformError = "Platform cannot be empty";
    }

    return errors;
}