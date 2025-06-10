const transcriptionTextarea = document.getElementById('transcription');
const startButton = document.getElementById('start-recording');
const stopButton = document.getElementById('stop-recording');
const correctButton = document.getElementById('correct-grammar');
const summarizeButton = document.getElementById('summarize'); // Summarize button
const summaryLengthSlider = document.getElementById('summary-length'); // Slider for summary length
const typedCursor = document.querySelector('.typed-cursor'); // For cursor animation

let recognition; // SpeechRecognition instance

// Check if SpeechRecognition is supported
if ('SpeechRecognition' in window || 'webkitSpeechRecognition' in window) {
    const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;
    recognition = new SpeechRecognition();
    recognition.lang = 'en-US';
    recognition.continuous = true;

    recognition.onresult = (event) => {
        let transcript = '';
        for (let i = event.resultIndex; i < event.results.length; i++) {
            transcript += event.results[i][0].transcript;
        }
        animateText(transcriptionTextarea, transcript + ' '); // Append new speech text with animation
    };

    recognition.onerror = (event) => {
        console.error('Speech recognition error:', event.error);
        alert('An error occurred while recording: ' + event.error);
        resetButtons(); // Reset buttons in case of error
    };

    recognition.onend = () => {
        resetButtons(); // Ensure buttons reset when recognition ends
    };
} else {
    alert('Speech recognition is not supported in this browser.');
}

// Start recording
startButton.addEventListener('click', () => {
    // Remove this line to keep the text intact when starting
    // transcriptionTextarea.value = ''; // Clear the textarea at the start

    recognition.start();
    startButton.style.pointerEvents = 'none'; // Disable Record button
    startButton.style.opacity = 0.5; // Grey out Record button
    stopButton.style.pointerEvents = 'auto'; // Enable Stop button
    stopButton.style.opacity = 1;
});

// Stop recording
stopButton.addEventListener('click', () => {
    recognition.stop();
    resetButtons(); // Reset buttons when stopped
});

// Grammar correction button functionality
correctButton.addEventListener('click', async () => {
    const text = transcriptionTextarea.value.trim();
    if (!text) {
        alert('Please provide some text for grammar correction.');
        return;
    }

    try {
        const response = await fetch('GrammarCorrectionServlet', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ text })
        });

        if (!response.ok) {
            throw new Error(`Server error: ${response.statusText}`);
        }

        const data = await response.json();
        animateText(transcriptionTextarea, data.correctedText, true); // Replace text for grammar correction
    } catch (error) {
        console.error('Error correcting grammar:', error);
        alert('Failed to correct grammar. Please try again later.');
    }
});
// Event listener for the summarize button
summarizeButton.addEventListener('click', () => {
    const text = transcriptionTextarea.value.trim();
    const summaryLength = summaryLengthSlider.value;

    if (!text) {
        alert('Please provide some text for summarization.');
        return;
    }

    // Create a form to submit the data
    const form = document.createElement('form');
    form.method = 'POST';
    form.action = 'SummarizeServlet';

    // Create hidden input fields to hold the text and summary length
    const textInput = document.createElement('input');
    textInput.type = 'hidden';
    textInput.name = 'text';
    textInput.value = text;

    const summaryLengthInput = document.createElement('input');
    summaryLengthInput.type = 'hidden';
    summaryLengthInput.name = 'summaryLength';
    summaryLengthInput.value = summaryLength;

    // Append inputs to the form
    form.appendChild(textInput);
    form.appendChild(summaryLengthInput);

    // Append form to the body and submit it
    document.body.appendChild(form);
    form.submit();
});


// Function to reset buttons
function resetButtons() {
    startButton.style.pointerEvents = 'auto'; // Enable Record button
    startButton.style.opacity = 1; // Reset Record button style
    stopButton.style.pointerEvents = 'none'; // Disable Stop button
    stopButton.style.opacity = 0.5; // Grey out Stop button
}

/**
 * Function to animate text into the textarea
 * @param {HTMLElement} element - The textarea element
 * @param {string} text - The text to animate
 * @param {boolean} replace - Whether to replace or append text
 */
function animateText(element, text, replace = false) {
    const currentText = replace ? '' : element.value; // Keep existing text unless replacing
    let index = 0;
    const fullText = currentText + text;
    element.value = currentText; // Set initial value to current text

    const interval = setInterval(() => {
        if (index < text.length) {
            element.value += text[index];
            index++;
        } else {
            clearInterval(interval);
        }
    }, 50); // Adjust typing speed as needed

    // Show the typed cursor animation
    typedCursor.style.display = 'inline-block';
    typedCursor.style.animation = 'blink-cursor 0.7s step-end infinite';

    // Hide the cursor when animation completes
    setTimeout(() => {
        typedCursor.style.display = 'none';
    }, fullText.length * 50); // Hide the cursor based on animation duration
}
