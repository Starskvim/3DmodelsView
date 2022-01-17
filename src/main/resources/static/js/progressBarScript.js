
    // var elem = document.getElementById("myBar");
    var elem2 = document.getElementById("myBarTask");
    var elem3 = document.getElementById("barsTable")
    var elem4 = document.getElementById("myBar2")

    var invisibleLoadElem = document.getElementsByClassName("invisibleLoad")

    var width = 10;

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    console.log("sss")
    getResponse = async () => {
        let response = await fetch("http://localhost:8189/3Dmodel/updateProgressBar");

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
        // elem3.style.display = "contents"

        elem3.classList.remove("invisibleLoad")

        // console.log(progressOBJ)
        // console.log(progressOBJ.currentCount)
        // console.log(progressOBJ.currentTask)

        while (progress < 100) {
            progressOBJ = await getResponse()
            // console.log(progress)

            progress = progressOBJ.currentCount
            task = progressOBJ.currentTask

            // elem.style.width = progress + "%";
            // elem.innerHTML = progress + "%";

            elem4.style.width = progress + "%";
            elem4.innerHTML = progress + "%";

            console.log(progress)
            console.log(progress + "%")

            elem2.innerHTML = task;

            await sleep(300)
        }

    }