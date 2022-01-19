
    var elem2 = document.getElementById("myBarTask");
    var elem3 = document.getElementById("barsTable")
    var elem4 = document.getElementById("myBar2")

    var invisibleLoadElem = document.getElementsByClassName("invisibleLoad")

    var statsTable = document.getElementById("statsTable")
    var statsModels = document.getElementById("statsModels")
    var statsZIP = document.getElementById("statsZIP")
    var statsOTH = document.getElementById("statsOTH")
    var statsSIZE = document.getElementById("statsSIZE")



    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    getResponse = async () => {
        let response = await fetch("http://localhost:8189/3Dmodel/updateProgressBar");

        if (response.ok) {
            let obj = await response.json()
            return obj
        } else {
            return -1
        }
    }

    getResponseStats = async () => {
        let response = await fetch("http://localhost:8189/3Dmodel/stats");
        if (response.ok) {
            let obj = await response.json()
            return obj
        } else {
            return -1
        }
    }

    const test = async () => {
        let progressOBJ = await getResponse()
        let progress = progressOBJ.currentCount
        let task = progressOBJ.currentTask

        elem3.classList.remove("invisibleLoad")

        // console.log(progressOBJ)

        while (progress < 100) {
            progressOBJ = await getResponse()
            progress = progressOBJ.currentCount
            task = progressOBJ.currentTask
            elem4.style.width = progress + "%";
            elem4.innerHTML = progress + "%";
            console.log(progress)
            console.log(progress + "%")
            elem2.innerHTML = task;
            await sleep(300)
        }
    }

    const stats = async () => {
        let statsDB = await getResponseStats()

        let Models = statsDB.totalModels
        let ZIP = statsDB.totalZIP
        let OTH = statsDB.totalZIP
        let SIZE = statsDB.totalSize

        statsModels.innerHTML = Models
        statsZIP.innerHTML = ZIP
        statsOTH.innerHTML = OTH
        statsSIZE.innerHTML = SIZE + " GB"

        statsTable.classList.remove("invisibleLoad")

    }