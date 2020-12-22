<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

class CreateFotkaProblemuTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('fotka_problemu', function (Blueprint $table) {
            $table->bigIncrements('fotka_problemu_id');
            $table->text('cesta_k_suboru');
            $table->collation = 'utf8mb4_slovak_ci';

        });

        Schema::table('fotka_problemu', function (Blueprint $table) {

            $table->bigInteger('problem_id')->unsigned();


            $table->foreign('problem_id')
                ->references('problem_id')->on('problem')
                ->onDelete('cascade');

            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('fotka_problemu');
    }
}
