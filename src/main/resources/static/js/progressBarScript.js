
    var elem = document.getElementById("myBar");
    var width = 10;

    function sleep(ms) {
        return new Promise(resolve => setTimeout(resolve, ms));
    }

    console.log("sss")
    getResponse = async () => {
        let response = await fetch("http://localhost:8189/3Dmodel/updateProgressBar");

        if (response.ok) {
            let text = await response.text()
            return text
        } else {
            return -1
        }
    }

    const test = async () => {
        let progress = await getResponse()
        while (progress < 100) {
            progress = await getResponse()
            console.log(progress)

            elem.style.width = progress + "%";
            elem.innerHTML = progress + "%";


            await sleep(300)
        }

    }

// onload()