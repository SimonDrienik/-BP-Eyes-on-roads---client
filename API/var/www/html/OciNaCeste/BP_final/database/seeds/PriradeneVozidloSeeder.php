<?php

use App\Models\PriradeneVozidlo;
use Illuminate\Database\Seeder;

class PriradeneVozidloSeeder extends Seeder
{
    /**
     * Run the database seeds.
     *
     * @return void
     */
    public function run()
    {
        $priradene = [
            [
                'problem_id' => 1,
                'vozidlo_id' => 1,
            ],

        ];

        foreach ($priradene as $priradene_vozidlo) {
            PriradeneVozidlo::create(array(
                'problem_id' => $priradene_vozidlo['problem_id'],
                'vozidlo_id' => $priradene_vozidlo['vozidlo_id']
            ));
        }
    }
}
