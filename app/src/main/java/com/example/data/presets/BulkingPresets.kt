package com.example.data.presets

data class BulkingMeal(
    val id: String,
    val title: String,
    val category: String, // "SHAKES", "BREAKFAST", "LUNCH", "DINNER", "SNACKS"
    val dietaryType: String, // "ALL", "VEGETARIAN", "VEGAN"
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val prepTimeMin: Int,
    val ingredients: List<String>,
    val instructions: String,
    val bulkingBenefit: String
)

data class BulkingExercise(
    val name: String,
    val targetMuscle: String, // "CHEST", "BACK", "SHOULDERS", "ARMS", "LEGS", "CORE"
    val sets: Int,
    val repRange: String,
    val restSeconds: Int,
    val formCues: String,
    val bulkingTip: String
)

data class WorkoutRoutine(
    val id: String,
    val title: String,
    val subtitle: String,
    val splitType: String, // "PPL", "UPPER_LOWER", "FULL_BODY", "ARNOLD"
    val daysPerWeek: Int,
    val difficulty: String,
    val focusMuscles: List<String>,
    val exercises: List<BulkingExercise>
)

data class RecoveryProtocol(
    val id: String,
    val title: String,
    val tag: String,
    val iconName: String,
    val summary: String,
    val detailedTips: List<String>
)

data class QuickFoodItem(
    val name: String,
    val servingSize: String,
    val calories: Int,
    val proteinG: Int,
    val carbsG: Int,
    val fatG: Int,
    val defaultMealType: String
)

object BulkingPresets {

    val QUICK_FOODS = listOf(
        QuickFoodItem("Titan Monster Mass Shake", "1 Giant Shake (750ml)", 950, 52, 115, 26, "BULK_SHAKE"),
        QuickFoodItem("Rolled Oats & Peanut Butter", "100g oats + 35g PB", 580, 22, 75, 20, "BREAKFAST"),
        QuickFoodItem("4 Whole Large Eggs + 2 Toast", "4 eggs + 2 sourdough", 480, 28, 30, 22, "BREAKFAST"),
        QuickFoodItem("Grilled Chicken Breast & Jasmine Rice", "200g chicken + 250g rice", 640, 58, 70, 7, "LUNCH"),
        QuickFoodItem("Paneer Tikka / Bhurji & Paratha", "180g paneer + 2 parathas", 720, 38, 55, 36, "LUNCH"),
        QuickFoodItem("Ground Beef & Whole Grain Pasta", "180g beef (90/10) + 100g pasta", 760, 54, 80, 22, "DINNER"),
        QuickFoodItem("Salmon Fillet & Roasted Sweet Potato", "200g salmon + 300g sweet potato", 680, 46, 62, 24, "DINNER"),
        QuickFoodItem("Greek Yogurt Bowl + Almonds & Honey", "250g greek yogurt + honey + nuts", 420, 30, 42, 14, "SNACK"),
        QuickFoodItem("Whole Milk", "500 ml", 310, 16, 24, 16, "SNACK"),
        QuickFoodItem("Whey Protein Shake with Banana", "1 scoop whey + 1 large banana + water", 230, 26, 32, 2, "BULK_SHAKE"),
        QuickFoodItem("Soya Chunks Stir Fry with Rice", "80g soya chunks + 200g rice", 590, 48, 78, 6, "DINNER"),
        QuickFoodItem("Handful of Mixed Nuts (Walnuts, Almonds)", "50g", 315, 9, 10, 28, "SNACK")
    )

    val MEAL_PLANS = listOf(
        BulkingMeal(
            id = "m1",
            title = "Titan Anabolic Monster Shake",
            category = "SHAKES",
            dietaryType = "ALL",
            calories = 980,
            proteinG = 55,
            carbsG = 120,
            fatG = 28,
            prepTimeMin = 5,
            ingredients = listOf(
                "500ml Whole Milk (or Oat Milk)",
                "100g Rolled Oats (ground into powder)",
                "2 tbsp Natural Peanut Butter (32g)",
                "1 Large Ripe Banana",
                "1 Scoop Whey or Plant Protein (30g)",
                "1 tbsp Chia Seeds or Honey"
            ),
            instructions = "Blend oats into a fine flour first. Add whole milk, banana, peanut butter, and protein powder. Pulse on high speed for 60 seconds until creamy and lump-free. Drink post-workout or between meals.",
            bulkingBenefit = "Liquid calories are digested easily without feeling painfully full, making this the #1 secret weapon for hardgainers."
        ),
        BulkingMeal(
            id = "m2",
            title = "High-Voltage Power Scramble & Toast",
            category = "BREAKFAST",
            dietaryType = "ALL",
            calories = 780,
            proteinG = 46,
            carbsG = 65,
            fatG = 34,
            prepTimeMin = 12,
            ingredients = listOf(
                "4 Whole Free-Range Eggs",
                "2 Egg Whites",
                "50g Shredded Mozzarella or Cheddar",
                "2 Thick Slices Sourdough Bread",
                "1/2 Hass Avocado (mashed)",
                "1 tbsp Extra Virgin Olive Oil"
            ),
            instructions = "Whisk whole eggs and whites with a splash of milk. Scramble gently in olive oil over medium-low heat. Toast sourdough until golden, spread mashed avocado, top with fluffy eggs and melted cheese.",
            bulkingBenefit = "Whole egg yolks provide cholesterol and healthy fats crucial for testosterone synthesis and anabolic hormonal balance."
        ),
        BulkingMeal(
            id = "m3",
            title = "Hypertrophy Chicken & Jasmine Rice Feast",
            category = "LUNCH",
            dietaryType = "ALL",
            calories = 860,
            proteinG = 62,
            carbsG = 105,
            fatG = 18,
            prepTimeMin = 25,
            ingredients = listOf(
                "220g Chicken Breast (diced & seasoned)",
                "300g Cooked Jasmine Rice",
                "1 cup Steamed Broccoli florets",
                "1 tbsp Olive Oil or Sesame Oil",
                "2 tbsp Teriyaki or Honey Garlic glaze"
            ),
            instructions = "Pan-sear seasoned chicken in olive oil until golden brown. Steam jasmine rice. Toss chicken in glaze and serve over steaming jasmine rice with broccoli for micronutrients.",
            bulkingBenefit = "Jasmine rice has a high glycemic index that swiftly replenishes depleted muscle glycogen and triggers insulin for nutrient uptake."
        ),
        BulkingMeal(
            id = "m4",
            title = "Paneer & Lentil Mass Power Bowl",
            category = "LUNCH",
            dietaryType = "VEGETARIAN",
            calories = 840,
            proteinG = 48,
            carbsG = 95,
            fatG = 30,
            prepTimeMin = 20,
            ingredients = listOf(
                "180g Fresh Malai Paneer (cubed)",
                "1 cup Cooked Yellow Lentils / Dal",
                "250g Cooked Basmati Rice or 2 Whole Wheat Rotis",
                "1 tbsp Pure Ghee",
                "Spices: Cumin, Turmeric, Garam Masala"
            ),
            instructions = "Sauté paneer cubes in pure ghee with aromatic spices until edges are crisp. Heat prepared lentil dal. Serve over warm basmati rice and drizzle with golden ghee.",
            bulkingBenefit = "Combining paneer casein with lentils and rice yields a complete amino acid spectrum with high caloric density from healthy dairy fats."
        ),
        BulkingMeal(
            id = "m5",
            title = "Steak & Loaded Sweet Potato Anabolic Plate",
            category = "DINNER",
            dietaryType = "ALL",
            calories = 920,
            proteinG = 65,
            carbsG = 82,
            fatG = 36,
            prepTimeMin = 30,
            ingredients = listOf(
                "220g Sirloin Steak or Lean Beef Mince",
                "350g Baked Sweet Potato",
                "1 tbsp Grass-fed Butter",
                "Grilled Asparagus Spears",
                "Sea Salt & Cracked Black Pepper"
            ),
            instructions = "Sear steak in a blazing hot cast-iron skillet for 3-4 minutes per side. Bake sweet potato until tender, slice open and melt grass-fed butter into the flesh.",
            bulkingBenefit = "Red meat is loaded with natural creatine, bioavailable heme-iron, zinc, and B-vitamins that stimulate maximum muscle fiber repair."
        ),
        BulkingMeal(
            id = "m6",
            title = "Soya Chunk & Chickpea Anabolic Biryani",
            category = "DINNER",
            dietaryType = "VEGAN",
            calories = 810,
            proteinG = 52,
            carbsG = 110,
            fatG = 16,
            prepTimeMin = 25,
            ingredients = listOf(
                "85g High-Protein Soya Chunks (boiled & drained)",
                "150g Cooked Chickpeas",
                "250g Fragrant Basmati Rice",
                "1 tbsp Coconut or Olive Oil",
                "Mint, Fried Onions & Biryani Masala"
            ),
            instructions = "Marinate boiled soya chunks in spices. Sauté with chickpeas and onions. Layer with par-cooked basmati rice and steam on low heat for 10 minutes.",
            bulkingBenefit = "Soya chunks contain over 52% pure protein per 100g, making this one of the most cost-effective mass builders in existence."
        ),
        BulkingMeal(
            id = "m7",
            title = "Overnight Slow-Release Casein Pudding",
            category = "SNACKS",
            dietaryType = "VEGETARIAN",
            calories = 540,
            proteinG = 42,
            carbsG = 48,
            fatG = 18,
            prepTimeMin = 5,
            ingredients = listOf(
                "250g Full-Fat Greek Yogurt or Cottage Cheese",
                "1 Scoop Micellar Casein or Whey Protein",
                "1 tbsp Raw Honey",
                "20g Crushed Walnuts & Cacao Nibs"
            ),
            instructions = "Stir protein powder thoroughly into Greek yogurt until velvety smooth. Drizzle with raw honey and top with walnuts and cacao nibs. Consume 45 minutes before sleep.",
            bulkingBenefit = "Provides a slow, sustained drip of amino acids throughout 7-8 hours of sleep to prevent muscle catabolism during the overnight fast."
        )
    )

    val WORKOUT_ROUTINES = listOf(
        WorkoutRoutine(
            id = "r_push",
            title = "Push Matrix: Hypertrophy Overload",
            subtitle = "Chest, Front/Side Delts, & Triceps Destruction",
            splitType = "PPL",
            daysPerWeek = 6,
            difficulty = "INTERMEDIATE",
            focusMuscles = listOf("CHEST", "SHOULDERS", "ARMS"),
            exercises = listOf(
                BulkingExercise(
                    name = "Flat Barbell Bench Press",
                    targetMuscle = "CHEST",
                    sets = 4,
                    repRange = "6 - 8",
                    restSeconds = 120,
                    formCues = "Retract scapulae, plant feet firmly, control the descent for 2 seconds, drive explosively from chest.",
                    bulkingTip = "Compound baseline for raw chest mass. Strive to add 1kg or 1 extra rep every session."
                ),
                BulkingExercise(
                    name = "Incline Dumbbell Press",
                    targetMuscle = "CHEST",
                    sets = 3,
                    repRange = "8 - 10",
                    restSeconds = 90,
                    formCues = "Set bench to 30 degrees. Feel a deep stretch in the clavicular upper pec fibers before pressing.",
                    bulkingTip = "Upper chest fullness gives that dense 3D armor plate aesthetic."
                ),
                BulkingExercise(
                    name = "Standing Overhead Military Press",
                    targetMuscle = "SHOULDERS",
                    sets = 3,
                    repRange = "6 - 8",
                    restSeconds = 120,
                    formCues = "Brace core and glutes tightly. Press bar in a straight vertical bar path clearing the chin.",
                    bulkingTip = "Heavy vertical pressing builds thick cannonball anterior deltoids."
                ),
                BulkingExercise(
                    name = "Dumbbell Lateral Raises (Heavy Partial + Full)",
                    targetMuscle = "SHOULDERS",
                    sets = 4,
                    repRange = "12 - 15",
                    restSeconds = 60,
                    formCues = "Lead with the elbows, slight forward lean, pause for 0.5s at peak contraction.",
                    bulkingTip = "Lateral delts respond best to metabolic stress and strict mind-muscle connection."
                ),
                BulkingExercise(
                    name = "Weighted Dips (or Bodyweight to Failure)",
                    targetMuscle = "CHEST",
                    sets = 3,
                    repRange = "8 - 12",
                    restSeconds = 90,
                    formCues = "Lean chest forward 30 degrees to bias lower/mid pec fibers, descend to 90 degrees elbow bend.",
                    bulkingTip = "Dips are the squat of the upper body—an exceptional multi-joint mass builder."
                ),
                BulkingExercise(
                    name = "Overhead Cable Rope Tricep Extension",
                    targetMuscle = "ARMS",
                    sets = 3,
                    repRange = "10 - 12",
                    restSeconds = 60,
                    formCues = "Keep elbows pinned near ears. Fully stretch long head of the tricep in bottom position.",
                    bulkingTip = "The long head makes up 60% of total arm circumference."
                )
            )
        ),
        WorkoutRoutine(
            id = "r_pull",
            title = "Pull Grid: V-Taper Mass Builder",
            subtitle = "Lats, Upper Back, Traps, & Biceps",
            splitType = "PPL",
            daysPerWeek = 6,
            difficulty = "INTERMEDIATE",
            focusMuscles = listOf("BACK", "ARMS"),
            exercises = listOf(
                BulkingExercise(
                    name = "Conventional Deadlift",
                    targetMuscle = "BACK",
                    sets = 3,
                    repRange = "5 - 6",
                    restSeconds = 180,
                    formCues = "Hips wedged, lats engaged to take slack out of bar, push floor away through heels.",
                    bulkingTip = "Supreme CNS stimulation and spinal erector thickness that triggers systemic growth."
                ),
                BulkingExercise(
                    name = "Barbell Bent-Over Row",
                    targetMuscle = "BACK",
                    sets = 4,
                    repRange = "6 - 8",
                    restSeconds = 120,
                    formCues = "Torso at 45 degrees, pull bar toward belly button using lats rather than pulling with arms.",
                    bulkingTip = "Creates dense back depth and thickness so shirts stretch across your lats."
                ),
                BulkingExercise(
                    name = "Weighted Pull-Ups / Lat Pulldown",
                    targetMuscle = "BACK",
                    sets = 3,
                    repRange = "8 - 10",
                    restSeconds = 90,
                    formCues = "Full hang stretch at top, pull elbows down toward hips, drive chest upward.",
                    bulkingTip = "Widens your lat span for that iconic futuristic V-taper physique."
                ),
                BulkingExercise(
                    name = "Chest-Supported Dumbbell Rear Delt Row",
                    targetMuscle = "SHOULDERS",
                    sets = 3,
                    repRange = "12 - 15",
                    restSeconds = 60,
                    formCues = "Flare elbows outward at 70 degrees, squeeze shoulder blades together at top.",
                    bulkingTip = "Prevents rounded shoulders and balances heavy bench press volume."
                ),
                BulkingExercise(
                    name = "Standing Incline Dumbbell Bicep Curls",
                    targetMuscle = "ARMS",
                    sets = 3,
                    repRange = "8 - 10",
                    restSeconds = 75,
                    formCues = "Supinate wrists at the top of movement, eliminate swinging, 3-second negative descent.",
                    bulkingTip = "Incline angle stretches the long head of the bicep for maximal peak development."
                ),
                BulkingExercise(
                    name = "Hammer Curls with Dumbbells",
                    targetMuscle = "ARMS",
                    sets = 3,
                    repRange = "10 - 12",
                    restSeconds = 60,
                    formCues = "Neutral grip (palms facing inward). Squeeze the brachialis muscle beneath the bicep.",
                    bulkingTip = "Pushes the bicep peak upward and thickens forearm width significantly."
                )
            )
        ),
        WorkoutRoutine(
            id = "r_legs",
            title = "Leg Kinetic: The Anabolic Engine",
            subtitle = "Quads, Hamstrings, Glutes & Calves",
            splitType = "PPL",
            daysPerWeek = 6,
            difficulty = "ADVANCED",
            focusMuscles = listOf("LEGS", "CORE"),
            exercises = listOf(
                BulkingExercise(
                    name = "Barbell Back Squat",
                    targetMuscle = "LEGS",
                    sets = 4,
                    repRange = "6 - 8",
                    restSeconds = 150,
                    formCues = "Break at hips and knees simultaneously, achieve parallel depth, drive through midfoot.",
                    bulkingTip = "Stimulates massive systemic growth hormone release. Do not skip leg day during bulk!"
                ),
                BulkingExercise(
                    name = "Romanian Deadlift (RDL)",
                    targetMuscle = "LEGS",
                    sets = 3,
                    repRange = "8 - 10",
                    restSeconds = 120,
                    formCues = "Slight knee bend, hinge at hips sending pelvis backward until deep hamstring stretch.",
                    bulkingTip = "Builds heavy posterior chain mass and reinforces back strength for all lifts."
                ),
                BulkingExercise(
                    name = "Leg Press (High & Wide Foot Placement)",
                    targetMuscle = "LEGS",
                    sets = 3,
                    repRange = "10 - 12",
                    restSeconds = 90,
                    formCues = "Control carriage down slowly without lifting lower back from pad. Drive through heels.",
                    bulkingTip = "Allows pure quad overload without spinal compression fatigue."
                ),
                BulkingExercise(
                    name = "Lying Leg Hamstring Curls",
                    targetMuscle = "LEGS",
                    sets = 3,
                    repRange = "10 - 12",
                    restSeconds = 60,
                    formCues = "Keep hips pinned to pad, curl heels to glutes, hold 1 second isometric squeeze.",
                    bulkingTip = "Isolates knee flexion of hamstrings for balanced leg fullness from the side profile."
                ),
                BulkingExercise(
                    name = "Standing Heavy Calf Raises",
                    targetMuscle = "LEGS",
                    sets = 4,
                    repRange = "12 - 15",
                    restSeconds = 60,
                    formCues = "Full 2-second stretch at bottom, explode onto balls of feet, hold 1-second top squeeze.",
                    bulkingTip = "Calves need deep eccentric stretches under load to stimulate new myofibrils."
                )
            )
        ),
        WorkoutRoutine(
            id = "r_upper_lower",
            title = "Upper Titan: 4-Day Hardgainer Split",
            subtitle = "Optimal Frequency & Recovery for Fast Weight Gain",
            splitType = "UPPER_LOWER",
            daysPerWeek = 4,
            difficulty = "BEGINNER_TO_INTERMEDIATE",
            focusMuscles = listOf("CHEST", "BACK", "SHOULDERS", "ARMS"),
            exercises = listOf(
                BulkingExercise(
                    name = "Incline Barbell Bench Press",
                    targetMuscle = "CHEST",
                    sets = 4,
                    repRange = "6 - 8",
                    restSeconds = 120,
                    formCues = "Lower to upper chest, touch collarbone softly, press straight up.",
                    bulkingTip = "Prioritize upper chest first when energy and glycogen levels are highest."
                ),
                BulkingExercise(
                    name = "Weighted T-Bar Row",
                    targetMuscle = "BACK",
                    sets = 4,
                    repRange = "8 - 10",
                    restSeconds = 90,
                    formCues = "Keep spine neutral, pull handles tight to torso, squeeze lats and mid-traps.",
                    bulkingTip = "Massive mid-back density builder."
                ),
                BulkingExercise(
                    name = "Seated Dumbbell Shoulder Press",
                    targetMuscle = "SHOULDERS",
                    sets = 3,
                    repRange = "8 - 10",
                    restSeconds = 90,
                    formCues = "Palms angled slightly inward at 45 degrees, press dumbbells overhead in an arc.",
                    bulkingTip = "Stable seating eliminates cheating, isolating the shoulder cap."
                ),
                BulkingExercise(
                    name = "EZ-Bar Preacher Curls",
                    targetMuscle = "ARMS",
                    sets = 3,
                    repRange = "10 - 12",
                    restSeconds = 60,
                    formCues = "Arms anchored to pad, curl up smoothly without lifting shoulders off bench.",
                    bulkingTip = "Strict isolation that fills the lower bicep gap near the elbow."
                ),
                BulkingExercise(
                    name = "Skull Crushers (Lying Tricep Extension)",
                    targetMuscle = "ARMS",
                    sets = 3,
                    repRange = "10 - 12",
                    restSeconds = 60,
                    formCues = "Lower EZ-bar behind forehead to maintain constant tension at the top.",
                    bulkingTip = "Legendary arm builder used by elite bodybuilders for horseshoe tricep shape."
                )
            )
        )
    )

    val RECOVERY_PROTOCOLS = listOf(
        RecoveryProtocol(
            id = "rec_sleep",
            title = "Deep Sleep Growth Phase",
            tag = "HORMONES",
            iconName = "bedtime",
            summary = "70%+ of natural human growth hormone (HGH) is released during Stage 3 & 4 deep REM sleep.",
            detailedTips = listOf(
                "Aim for 8 to 9 hours of uninterrupted sleep every single night during a bulk.",
                "Keep room temperature cool (65-68°F / 18-20°C) to facilitate core body temperature drop.",
                "Avoid screens 45 minutes before bed or use blue light filters to preserve melatonin.",
                "Take 300mg Magnesium Glycinate and 30mg Zinc 30 minutes prior to bedtime for deeper sleep."
            )
        ),
        RecoveryProtocol(
            id = "rec_rest_nutrition",
            title = "Rest Day Anabolism Rule",
            tag = "NUTRITION",
            iconName = "restaurant",
            summary = "Do NOT cut calories on rest days! Muscle repair requires surplus energy 48 hours post-workout.",
            detailedTips = listOf(
                "Maintain your full bulking caloric surplus on non-training days. Muscles grow while resting, not in the gym!",
                "Distribute protein intake evenly across 4 to 5 meals (30-40g every 3 to 4 hours) to sustain muscle protein synthesis.",
                "Slightly shift carbs toward complex slow-digesting sources (oats, brown rice, sweet potatoes, quinoa).",
                "Ensure at least 3.5 liters of pure water to keep muscle glycogen hydrated."
            )
        ),
        RecoveryProtocol(
            id = "rec_active_recovery",
            title = "Active Bloodflow & Deload",
            tag = "RESTORATION",
            iconName = "directions_walk",
            summary = "Gentle movement flushes metabolic waste products and delivers amino acids to healing muscle fibers.",
            detailedTips = listOf(
                "Perform 20-30 minutes of low-intensity walking or light cycling on rest days.",
                "Do 10 minutes of hip flexor, hamstring, and thoracic spine mobility work.",
                "Alternate hot and cold showers (2 min hot, 30s cold) to boost circulation and reduce chronic soreness.",
                "Take a scheduled deload week every 6 to 8 weeks by reducing training weights by 40%."
            )
        ),
        RecoveryProtocol(
            id = "rec_hydration_electrolytes",
            title = "Hypertrophic Hydration Cell Volume",
            tag = "HYDRATION",
            iconName = "water_drop",
            summary = "A dehydrated muscle cell is catabolic. Cellular swelling from hydration triggers anabolic pathways.",
            detailedTips = listOf(
                "Every gram of muscle glycogen stored draws approximately 3 to 4 grams of water into the muscle cell.",
                "Consume 500ml water immediately upon waking to reverse overnight dehydration.",
                "Add a pinch of sea salt / pink Himalayan salt to pre-workout meals for enhanced pumps and electrolyte balance.",
                "Take 5 grams of Creatine Monohydrate daily at the same time to maximize intracellular water retention."
            )
        )
    )

    val SMART_COACH_TIPS = listOf(
        "⚡ Progressive Overload is King: If you aren't lifting heavier or doing more reps over time, excess calories will turn into fat instead of muscle. Log every single set!",
        "🔥 The 300-500 Kcal Sweet Spot: A moderate daily surplus produces steady muscle gains of 0.25 - 0.5 kg (0.5 - 1 lb) per week with minimal fat accumulation.",
        "🥛 Don't Fear Liquid Calories: If you struggle to finish your meals, blend oats, milk, peanut butter, and bananas. You can easily drink 800-1000 calories in 2 minutes!",
        "💪 Rest Days Are Growth Days: You do not grow in the gym; you break muscle down in the gym. You grow in bed while sleeping in a caloric surplus.",
        "💎 Consistency Over Perfection: Missing one meal won't ruin your bulk, but consistently falling 500 calories short of your target will stall all gains."
    )
}
