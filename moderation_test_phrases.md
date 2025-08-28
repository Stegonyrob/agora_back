# Moderation Test Phrases - Comprehensive Guide

## ✅ Safe Phrases (Should NEVER be censored)

### Basic Greetings and Courtesy
- "hola"
- "buenos días"
- "buenas tardes"
- "¿cómo estás?"
- "gracias"
- "de nada"
- "hello"
- "good morning"
- "how are you?"
- "thank you"
- "you're welcome"
- "por favor"
- "disculpa"
- "perdón"

### Academic and Educational Content
- "me gusta esta explicación"
- "no entiendo este concepto"
- "¿puedes ayudarme?"
- "excelente trabajo"
- "muy interesante"
- "I like this explanation"
- "I don't understand this concept"
- "can you help me?"
- "excellent work"
- "very interesting"
- "¿alguien puede explicar esto?"
- "necesito ayuda con esto"
- "buen punto"

### Constructive Discussion
- "estoy de acuerdo"
- "no estoy de acuerdo pero respeto tu opinión"
- "interesante punto de vista"
- "¿qué opinas sobre esto?"
- "creo que hay otro enfoque"
- "I agree"
- "I disagree but respect your opinion"
- "interesting point of view"
- "what do you think about this?"
- "I think there's another approach"
- "me parece bien"
- "tienes razón"

### Positive Feedback
- "bien hecho"
- "felicitaciones"
- "me encanta"
- "perfecto"
- "genial"
- "well done"
- "congratulations"
- "I love it"
- "perfect"
- "great"
- "increíble trabajo"
- "sigue así"

## ❌ Offensive Phrases (Should be censored)

### Direct Insults and Personal Attacks
- "idiota"
- "estúpido"
- "imbécil"
- "tonto"
- "inútil"
- "stupid"
- "idiot"
- "moron"
- "loser"
- "worthless"
- "eres un inútil"
- "qué idiota eres"
- "vete a la mierda"
- "no sirves para nada"
- "cállate, estúpido"

### Profanity and Vulgar Language
- "mierda"
- "joder"
- "coño"
- "carajo"
- "shit"
- "fuck"
- "damn"
- "crap"
- "hijo de puta"
- "que te jodan"
- "pendejo"
- "gilipollas"
- "cabrón"
- "jódete"

### Harassment and Threats
- "te voy a matar"
- "vete al infierno"
- "no vales nada"
- "eres un desastre"
- "I'll kill you"
- "go to hell"
- "you're worthless"
- "you're a disaster"
- "te voy a encontrar"
- "vas a pagar por esto"

### Discriminatory Language
- "maricón" (homophobia)
- "maldito gay" (homophobia)
- "ese retrasado no entiende nada"
- "no seas mongólico"
- "pareces autista"
- "vete a tu país"
- "todos los inmigrantes son ladrones"
- "no quiero negros aquí"
- "los sudacas no entienden"
- "malditos moros"
- "las mujeres no deberían opinar aquí"
- "eso es cosa de maricas"

### Spam and Repetitive Content
- "aaaaaaaaaaaaaaaa"
- "hola hola hola hola hola"
- "click aquí para ganar dinero"
- "compra ahora"
- "oferta especial"
- "click here to earn money"
- "buy now"
- "special offer"
- "gana dinero rápido"
- "oportunidad única"

### Veiled Insults
- "qué curioso que siempre falles, como algunos..."
- "no esperaba mucho de ti, la verdad"
- "seguro que con tu 'inteligencia' no lo entiendes"
- "no me sorprende que no te salga, viendo de dónde eres"

## ⚠️ Edge Cases (Require careful analysis)

### Context-Dependent Phrases
- "esto es malo" (could be legitimate criticism)
- "no me gusta" (legitimate opinion vs. harassment)
- "this is bad" (legitimate criticism vs. negative)
- "I don't like it" (legitimate opinion vs. harassment)
- "qué desastre" (could refer to situation, not person)

### False Positives to Avoid (Legitimate expressions)
- "matar el tiempo" (idiomatic expression, not a threat)
- "estoy muerto de cansancio" (expression, not violence)
- "kill time" (idiomatic expression, not a threat)
- "I'm dead tired" (expression, not violence)
- "eso me mata de risa" (expression, not violence)
- "me muero de ganas" (expression, not violence)

### Academic Terms That Might Trigger False Positives
- "análisis crítico"
- "punto débil"
- "fallo en el sistema"
- "error fatal"
- "critical analysis"
- "weak point"
- "system failure"
- "fatal error"

## 🧪 Testing Guidelines

### Sentiment Analysis Testing
1. Test with different sentence structures
2. Test with mixed content (safe + offensive in same message)
3. Test with common typos and variations
4. Test with different languages (Spanish/English)
5. Test with emojis and special characters
6. Test with ALL CAPS and mixed case

### Word List Testing
1. Test exact matches
2. Test partial matches (substring detection)
3. Test case sensitivity
4. Test with special characters and numbers
5. Test leetspeak variations (3 instead of e, @ instead of a)
6. Test with spaces between letters (i d i o t a)

### Performance Testing
1. Test with very long messages (1000+ characters)
2. Test with repeated words
3. Test with special formatting (emojis, punctuation)
4. Test response time under load
5. Test memory usage with large phrase lists

## 📊 Moderation Tuning Recommendations

### To Reduce False Positives
- Implement context analysis (not just word matching)
- Use confidence scores for sentiment analysis
- Maintain whitelist of educational/academic terms
- Consider sentence structure and grammar
- Implement user reputation system
- Allow appeals process for false positives

### To Improve Detection
- Regular updates to offensive word lists
- Implement pattern detection for spam
- Use machine learning for context understanding
- Consider cultural and regional differences
- Monitor new slang and internet terminology
- Implement fuzzy matching for typos

### Monitoring and Metrics
- Track false positive rate (target: <5%)
- Track false negative rate (target: <2%)
- Monitor user complaints about censorship
- Regular review of censored content
- A/B testing of moderation strategies
- Performance metrics (response time, accuracy)

### Integration with UserViolation System
- Log all moderation actions with ViolationType
- Track user violation patterns
- Implement escalating consequences
- Generate reports for admin review
- Maintain audit trail for appeals

---

**Use this comprehensive guide to:**
- Tune moderation algorithms
- Create unit tests
- Train machine learning models
- Validate system performance
- Handle user appeals
- Update moderation policies
